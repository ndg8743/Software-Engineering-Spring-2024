package org.fibsters;

import org.fibsters.interfaces.ComputeJob;
import org.fibsters.interfaces.ComputeJobPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComputeJobPoolImpl implements ComputeJobPool {
    int maxNumThreads;
    ExecutorService executor; // 🤘

    public ComputeJobPoolImpl() {
        maxNumThreads = Runtime.getRuntime().availableProcessors();
        // TODO: possibly need to check maxNumThreads before any allocation depending on how distributed the system is. This approach might be too greedy.
        executor = Executors.newFixedThreadPool(maxNumThreads);
    }
    @Override
    public void addJob(ComputeJob job) {

    }

    @Override
    public void removeJob(ComputeJob job) {

    }

    @Override
    public void processJob(ComputeJob job) {

    }

    @Override
    public void processAllJobs() {

    }

    @Override
    public void getJobStatus(ComputeJob job) {

    }

    @Override
    public void getJobResults(ComputeJob job) {

    }
}
