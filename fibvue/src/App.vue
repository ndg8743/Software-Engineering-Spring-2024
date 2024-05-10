<style>
.fibImage {
  height: 50vh;
}
.loading {
  width: 3em;
}

.image-gallery {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}
</style>

<template>
  <div>
    <h1>Fibonacci Job Client</h1>
    <input v-model="userInput" placeholder="Enter numbers separated by commas">
    <button @click="startComputeJob">Start Compute Job</button>
    <div v-if="jobStatus">
      Job Status: {{ jobStatus }}
    </div>
    <div v-if="jobResult">
      <h2>fibCalcResults:</h2>
      <div v-for="(result, index) in jobResult.fibCalcResults" :key="index">
        {{ result }}
      </div>
      <h2>fibSpiralResults:</h2>
      <div class="image-gallery">
        <div v-for="(result, index) in jobResult.fibSpiralResults" :key="index">
          <a v-if="result" :href="'data:image/png;base64,' + result" target="_blank">
            <img :src="'data:image/png;base64,' + result" alt="Base64 Image" class="fibImage">
          </a>
          <img v-else src="./assets/Rocket.gif" alt="Loading" class="loading">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { useToast } from 'vue-toastification';

const toast = useToast();

export default {
  data() {
    return {
      userInput: '',
      jobId: null,
      jobStatus: null,
      jobResult: null,
    };
  },
  methods: {
    /*
    example response:
      {"data":{"uniqueID":"bcaf4de0-7234-4562-a5a4-b0ad52435a13","index":0,"inputPayload":{"directive":"SUBMIT_COMPUTE_JOB","payloadData":{"calcFibNumbersUpTo":[5,6]}},"status":"PENDING","fibCalcResults":[[0,0,0,0,0],[0,0,0,0,0,0]],"fibSpiralResults":[null,null],"fibCalcStrings":[[],[]]},"success":true}
    */
    async startComputeJob() {
      const inputNumbers = this.userInput.split(',').map(num => parseInt(num));
      const jobData = {
        directive: 'SUBMIT_COMPUTE_JOB',
        payloadData: {
          calcFibNumbersUpTo: inputNumbers,
          uniqueID: Math.random().toString(36).substring(7)  // Generate a random unique ID
        }
      };
      try {
        const response = await axios.post('http://localhost:8080/fib', jobData);
        const payload = response.data;
        this.jobId = payload.data.uniqueID;  // Assume response structure has uniqueID
        console.log('Job ID:', this.jobId);
        this.pollJobStatus();
      } catch (error) {
        console.error('Error starting compute job:', error);
      }
    },
    async pollJobStatus() {
      const statusData = {
        directive: 'GET_JOB_STATUS_BY_ID',
        uniqueID: this.jobId
      };
      try {
        const statusResponse = await axios.post('http://localhost:8080/fib', statusData);
        const payload = statusResponse.data;
        this.jobStatus = payload.data.status;
        if (this.jobStatus === 'COMPLETED') {
          this.fetchJobResults();

          toast.success('Fibonacci job completed!');
        } if (this.jobStatus === 'PENDING' || this.jobStatus === 'RUNNING') {
          this.jobResult = payload.data;
          setTimeout(this.pollJobStatus, 1000); // Poll every second
        }
      } catch (error) {
        console.error('Error polling job status:', error);
        toast.error("Error polling job status: " + error);
      }
    },
    async fetchJobResults() {
      const resultData = {
        directive: 'GET_JOB_BY_ID',
        uniqueID: this.jobId
      };
      try {
        const resultResponse = await axios.post('http://localhost:8080/fib', resultData);
        const payload = resultResponse.data;
        this.jobResult = payload.data;
        console.log('Job results:', this.jobResult);
      } catch (error) {
        console.error('Error fetching job results:', error);
      }
    }
  }
};
</script>
