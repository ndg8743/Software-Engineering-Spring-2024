package org.fibsters;

interface ComputeJobPool {
    // i.e. public class MyComputeJobPool implements ComputeJobPool { ... } blah blah
    void addJob(ComputeJob job);

    void removeJob(ComputeJob job);

    void processJob(ComputeJob job);

    void processAllJobs();

    void getJobStatus(ComputeJob job);

    void getJobResults(ComputeJob job);
}
