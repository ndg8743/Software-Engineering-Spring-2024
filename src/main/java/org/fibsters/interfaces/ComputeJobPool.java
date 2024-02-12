package org.fibsters.interfaces;

import org.fibsters.ComputeJobStatus;

public interface ComputeJobPool {
    // i.e. public class MyComputeJobPool implements ComputeJobPool { ... } blah blah
    void addJob(ComputeJob job);

    void removeJob(ComputeJob job);

    ComputeJobStatus getJobStatus(ComputeJob job);

    void getJobResults(ComputeJob job);
}
