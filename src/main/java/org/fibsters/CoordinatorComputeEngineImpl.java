package org.fibsters;

import org.fibsters.interfaces.*;

public class CoordinatorComputeEngineImpl implements CoordinatorComputeEngine {

    private final DataStorageImpl dataStorage;
    private final ComputeJobPoolImpl jobPool;

    public CoordinatorComputeEngineImpl(DataStorageImpl dataStorage) {
        this.dataStorage = dataStorage;

        this.jobPool = new ComputeJobPoolImpl();
        this.jobPool.start();
    }

    public String processInputStringForOutput(String inputPayloadString) {
        Result<InputPayloadImpl> inputPayloadResult = parseInputPayload(inputPayloadString);

        if (!inputPayloadResult.isSuccess()) {
            return (new FailureResult<>(null, "Failed to parse input payload: " + inputPayloadResult.getErrorMessage())).toJSONString();
        }

        InputPayloadImpl inputPayload = inputPayloadResult.getData();
        String id;

        switch (inputPayload.getDirective()) {
            case InputPayloadImpl.DirectiveType.GET_JOB_STATUS_BY_ID:
                id = inputPayload.getUniqueID();

                return (new SuccessResult<>(getJobById(id).getOutputPayload())).toJSONStringShallow();
            case InputPayloadImpl.DirectiveType.GET_JOB_BY_ID:
                id = inputPayload.getUniqueID();

                return (new SuccessResult<>(getJobById(id).getOutputPayload()).toJSONString());
            case InputPayloadImpl.DirectiveType.SUBMIT_COMPUTE_JOB:
                ComputeJob job = createComputeJobFromInputPayload(inputPayload);

                queueJob(job);

                return (new SuccessResult<>(job.getOutputPayload())).toJSONStringShallow();
            default:
                return (new FailureResult<>(null, "Invalid directive: " + inputPayload.getDirective())).toJSONString();
        }

    }

    @Override // @Override is not strictly needed, but it is good practice to use it
    public Result<InputPayloadImpl> parseInputPayload(String inputPayloadString) {
        return dataStorage.parseInputPayload(inputPayloadString);
    }

    @Override
    public Result<OutputPayload> parseOutputPayload(ComputeJob completedJob) {
        return null;
    }

    @Override
    public void jobPoolLoop() {

    }

    @Override
    public void payloadPoolLoop() {

    }

    @Override
    public ComputeJob createComputeJobFromInputPayload(InputPayloadImpl inputPayload) {
        OutputPayloadImpl outputPayload = new OutputPayloadImpl(0, inputPayload, ComputeJobStatus.UNSTARTED);
        MultipartComputeJob computeJob = new MultipartComputeJob(outputPayload);

        computeJob.setInputPayload(inputPayload);

        return computeJob;
    }

    @Override
    public void processCompletedJob(ComputeJob job) {

    }

    @Override
    public void queueJob(ComputeJob job) {
        jobPool.addJob(job);
    }

    @Override
    public ComputeJobStatus getJobStatus(ComputeJob job) {
        return job.getStatus();
    }


    public ComputeJobStatus getJobStatusById(String id) {
        return jobPool.getJobById(id).getStatus();
    }

    public ComputeJob getJobById(String id) {
        return jobPool.getJobById(id);
    }

}
