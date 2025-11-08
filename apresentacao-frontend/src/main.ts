import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { createVuetify } from 'vuetify';

import App from './App.vue'
import router from './router'


import '@mdi/font/css/materialdesignicons.css';
// import 'vuetify/styles'; // Import Vuetify styles
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';

const vuetify = createVuetify({
  components,
  directives,
  icons: {
    defaultSet: 'mdi',
  },
});

const store = createPinia()
const app = createApp(App)

app.use(vuetify)
app
.use(store)
.use(router)
.mount('#app')

