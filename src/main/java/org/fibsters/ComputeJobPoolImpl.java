package org.fibsters;

import org.fibsters.interfaces.ComputeJob;
import org.fibsters.interfaces.ComputeJobPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ComputeJobPoolImpl implements ComputeJobPool {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ExecutorService executor; // 🤘
    private final Queue<ComputeJob> jobs;

    private final ArrayList<ComputeJob> finishedJobs = new ArrayList<>();

    private ComputeJob currentJob;
    private final Future<?>[] futureFibTasks;

    public ComputeJobPoolImpl() {
        int maxNumThreads = getMaxNumThreads();

        this.executor = Executors.newFixedThreadPool(maxNumThreads);
        this.jobs = new ConcurrentLinkedQueue<>();
        this.futureFibTasks = new Future[maxNumThreads];

        // initialize the futureFibTasks array
        initializeFutureFibTasks();
    }

    private void initializeFutureFibTasks() {
        Arrays.fill(futureFibTasks, new Future<>() {
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
        });
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
        // check if job is multipart
        if (job instanceof MultipartComputeJob multiJob && job.getStatus() != ComputeJobStatus.COMPLETED) {
            // if it is, split the job into multiple subjobs
            if (multiJob.getFibCalcCE().getStatus() == ComputeJobStatus.COMPLETED) {
                if (multiJob.isSpiralFinished()) {
                    multiJob.getFibSpiralCE().saveBuffer();

                    multiJob.setStatus(ComputeJobStatus.COMPLETED);

                    this.currentJob = null;
                    return;
                } else {
                    if (multiJob.getFibSpiralCE().getStatus() == ComputeJobStatus.UNSTARTED) {
                        multiJob.getFibSpiralCE().setStatus(ComputeJobStatus.PENDING);

                        this.currentJob = multiJob.getFibSpiralCE();

                        // add multijob back to queue, so next job will be spiral
                        this.jobs.add(multiJob);
                    }
                }
            } else {
                multiJob.getFibCalcCE().setStatus(ComputeJobStatus.PENDING);

                this.currentJob = multiJob.getFibCalcCE();

                // add multijob back to queue, so next job will be spiral
                this.jobs.add(multiJob);
            }

            multiJob.setStatus(ComputeJobStatus.PENDING);

            job = this.currentJob;
        }

        job.setStatus(ComputeJobStatus.RUNNING);

        int numThreads = getMaxNumThreads();

        int[] subJobs = job.getInputPayload().getPayloadDataParsed(); // ex [1, 10, 25]

        int[] threadsPerSubjob = this.handleThreadPooling(subJobs, numThreads);

        int threadCount = 0;

        for (int i = 0; i < threadsPerSubjob.length; i++) {
            int threadGroupSize = subJobs[i] / threadsPerSubjob[i];

            for (int j = 0; j < threadsPerSubjob[i]; j++) {
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

    private int[] handleThreadPooling(int[] jobs, int numThreads) {
        int n = jobs.length;
        double totalJobs = Arrays.stream(jobs).sum();
        double[] proportions = Arrays.stream(jobs).asDoubleStream().map(job -> job / totalJobs).toArray();
        int[] distributedThreads = new int[n];
        Arrays.fill(distributedThreads, 1); // Start each job with one thread

        // Adjust total number of threads available for proportional distribution
        int availableThreads = numThreads - n;

        // Calculate proportional distribution of the remaining threads
        int[] additionalThreads = Arrays.stream(proportions).mapToInt(prop -> (int) (availableThreads * prop)).toArray();

        // Adjust for rounding errors in additional threads distribution
        int distributedSum = Arrays.stream(additionalThreads).sum();
        int difference = availableThreads - distributedSum;

        // Add additional threads to the distributedThreads
        for (int i = 0; i < n; i++) {
            distributedThreads[i] += additionalThreads[i];
        }

        // Distribute the remaining threads based on the largest residuals
        double[] residuals = new double[n];

        for (int i = 0; i < n; i++) {
            residuals[i] = availableThreads * proportions[i] - additionalThreads[i];
        }

        while (difference > 0) {
            int maxIndex = 0;

            for (int i = 1; i < residuals.length; i++) {
                if (residuals[i] > residuals[maxIndex]) {
                    maxIndex = i;
                }
            }

            distributedThreads[maxIndex]++;
            residuals[maxIndex] = 0; // Reset the max residual to prevent repeated increment
            difference--;
        }

        return distributedThreads;
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

        for (Future<?> futureFibTask : futureFibTasks) {
            if (futureFibTask.isDone()) {
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

    public ComputeJob getJobById(String id) {
        ComputeJob multiPartJob = isMultiPartComputeJob(id);

        if (multiPartJob != null) {
            return multiPartJob;
        }

        if (currentJob != null && Objects.equals(currentJob.getOutputPayload().getUniqueID(), id)) {
            return currentJob;
        }

        for (ComputeJob job : finishedJobs) {
            if (Objects.equals(job.getOutputPayload().getUniqueID(), id)) {
                return job;
            }
        }

        for (ComputeJob job : jobs) {
            if (Objects.equals(job.getOutputPayload().getUniqueID(), id)) {
                return job;
            }
        }

        return null;
    }

    public ComputeJob isMultiPartComputeJob(String id) {
        for (ComputeJob job : jobs) {
            if (job instanceof MultipartComputeJob && Objects.equals(job.getOutputPayload().getUniqueID(), id)) {
                return job;
            }
        }

        for (ComputeJob job : finishedJobs) {
            if (job instanceof MultipartComputeJob && Objects.equals(job.getOutputPayload().getUniqueID(), id)) {
                return job;
            }
        }

        return null;
    }

}
