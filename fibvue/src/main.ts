import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import toast from 'vue-toastification'
import "vue-toastification/dist/index.css";

const app = createApp(App)

app
    .use(toast, {})

app.mount('#app')
