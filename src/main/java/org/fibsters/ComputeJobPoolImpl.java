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
        // initialize the futureFibTasks array
        initializeFutureFibTasks();

    }

    private void initializeFutureFibTasks() {
        for (int i = 0; i < futureFibTasks.length; i++) {
            futureFibTasks[i] = new Future<Object>() {
                @Override
                public boolean cancel(boolean mayInterruptIfRunning) {
                    return false;
                }

                @Override
                public boolean isCancelled() {
                    return false;
                }

                @Override
                public boolean isDone() {
                    return false;
                }

                @Override
                public Object get() {
                    return null;
                }

                @Override
                public Object get(long timeout, TimeUnit unit) {
                    return null;
                }
            };
        }
    }


    @Override
    public void addJob(ComputeJob job) {
        job.setStatus(ComputeJobStatus.PENDING);
        jobs.add(job);
    }

    public int getMaxNumThreads() {
        return Runtime.getRuntime().availableProcessors();
    }

    @Override
    public void removeJob(ComputeJob job) {

    }

    public void start() {
        // TODO: make sure this starts only once. possible use the factory pattern
        scheduler.scheduleAtFixedRate(this::processAllJobs, 0, 1, TimeUnit.SECONDS);
    }

    // process jobs one at a time in queue, using all available threads per job
    private void processJob(ComputeJob job) {
        job.setStatus(ComputeJobStatus.RUNNING);
        int numThreads = getMaxNumThreads();

        //int threadGroupSize = Math.round(job.getTotalSize() / numThreads);
        int totalJobSize = job.getTotalSize(); // if [1, 10, 25] then 36
        int[] subJobs = job.getInputPayload().getPayloadDataParsed(); // ex [1, 10, 25]

        int[] threadsPerSubjob = new int[subJobs.length];

        int biggestSubJob = 0;
        int biggestSubJobIndex = 0;
        int sanityCheckCount = 0;
        for (int i = 0; i < subJobs.length; i++) {
            int threadNum = Math.max(1, Math.round(((float) subJobs[i] / totalJobSize) * numThreads));
            if (threadNum > biggestSubJob) {
                biggestSubJob = threadNum;
                biggestSubJobIndex = i;
            }
            sanityCheckCount += threadNum;
            threadsPerSubjob[i] = threadNum;
        }
        if (sanityCheckCount < numThreads) {
            int diff = Math.abs(numThreads - sanityCheckCount);
            threadsPerSubjob[0] += diff; // add the difference to the first subjob
        } else if (sanityCheckCount > numThreads) {
            int diff = Math.abs(numThreads - sanityCheckCount);
            threadsPerSubjob[biggestSubJobIndex] -= diff; // subtract the difference from the biggest subjob
        }
        int threadCount = 0;
        for (int i = 0; i < threadsPerSubjob.length; i++) {
            int threadGroupSize = subJobs[i] / threadsPerSubjob[i];
            for (int j = 0; j < threadsPerSubjob[i]; j++) {
                //System.out.println("Subjob " + i + " has " + threadsPerSubjob[i] + " threads");

                int start = j * threadGroupSize;
                int end = (j + 1) * threadGroupSize;

                if (j == threadsPerSubjob[i] - 1) { // threadgroup didnt divide evenly so pick up the remaining elements
                    end = subJobs[i];
                }

                ComputeJob jobClone = job.clone();
                jobClone.setStartIndex(start);
                jobClone.setEndIndex(end);
                jobClone.setChunk(i);
                futureFibTasks[threadCount++] = executor.submit(jobClone);
            }
        }
        System.out.println("Thread count: " + threadCount);

        /*int threadGroupSize = Math.round(job.getTotalSize() / numThreads); // 90 fib numbers / 4 threads = 22.5
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
        }*/
        //executor.execute(job);
    }

    private void processAllJobs() {
        if (!jobs.isEmpty()) {
            if (this.currentJob == null) {
                this.currentJob = jobs.poll();
                if (currentJob != null) {
                    processJob(currentJob);
                }
            }
        }
        // loop over futures
        int count = 0;
        for (int i = 0; i < futureFibTasks.length; i++) {
            if (futureFibTasks[i].isDone()) {
                count++;
            }
        }
        if (count == futureFibTasks.length) {
            initializeFutureFibTasks();
            currentJob.setStatus(ComputeJobStatus.COMPLETED);
            finishedJobs.add(currentJob);
            currentJob = null;
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
