package org.fibsters;

import org.fibsters.interfaces.ComputeJob;
import org.fibsters.interfaces.ComputeJobPool;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ComputeJobPoolImpl implements ComputeJobPool {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ExecutorService executor; // 🤘
    private Queue<ComputeJob> jobs;

    private final ArrayList<ComputeJob> finishedJobs = new ArrayList<>();

    private ComputeJob currentJob;
    Future<?>[] futureFibTasks;

    public ComputeJobPoolImpl() {
        int maxNumThreads = getMaxNumThreads();
        executor = Executors.newFixedThreadPool(maxNumThreads);
        jobs = new ConcurrentLinkedQueue<ComputeJob>();
        futureFibTasks = new Future[maxNumThreads];
    }
    @Override
    public void addJob(ComputeJob job) {
        jobs.add(job);
    }

    public int getMaxNumThreads() {
        return Runtime.getRuntime().availableProcessors();
    }

    @Override
    public void removeJob(ComputeJob job) {

    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::processAllJobs, 0, 5, TimeUnit.SECONDS);
    }

    // process jobs one at a time in queue, using all available threads per job
    private void processJob(ComputeJob job) {
        int numThreads = getMaxNumThreads();
        int threadGroupSize = job.getTotalSize() / numThreads; // 90 fib numbers / 4 threads = 22.5
        for (int i = numThreads; i >= 0; i--) {
            int start = i * threadGroupSize;
            int end = (i + 1) * threadGroupSize;

            if (i == numThreads - 1) { // threadgroup didnt divide evenly so pick up the remaining elements
                end = job.getTotalSize();
            }
            // important to set the start and end indices for the job so that it knows what to calculate
            job.setStartIndex(start);
            job.setEndIndex(end);
            futureFibTasks[i] = executor.submit(job);
        }
        executor.execute(job);
    }

    private void processAllJobs() {
        if (jobs.isEmpty()) {
            return;
        }
        if (currentJob == null || currentJob.getStatus() == ComputeJobStatus.COMPLETED) {
            finishedJobs.add(currentJob);
            currentJob = jobs.poll();
            if (currentJob != null) {
                processJob(currentJob);
            }
        }
        // if the current job is not done, then we need to check if it's done
        // loop over futures
        int count = 0;
        for (int i = 0; i < futureFibTasks.length; i++) {
            if (futureFibTasks[i].isDone()) {
                count++;
            }
        }
        if (count == futureFibTasks.length) {
            currentJob.setStatus(ComputeJobStatus.COMPLETED);
        }
    }

    @Override
    public ComputeJobStatus getJobStatus(ComputeJob job) {
        // check if job is in finishedJobs
        if (finishedJobs.contains(job)) {
            return ComputeJobStatus.COMPLETED;
        }
        if (job == currentJob) {
            return ComputeJobStatus.RUNNING;
        }
        return ComputeJobStatus.PENDING; // assume in queue
    }

    @Override
    public void getJobResults(ComputeJob job) {

    }
}
