<script setup lang="ts">
import ShowToast from '@/components/Common/ShowToast.vue';
import { storeToRefs } from 'pinia';
import { useNotificationStore } from './stores/notificationStore';

const notificationStore = useNotificationStore();
const { toastMessage, toastTitle, visible } = storeToRefs(notificationStore);

</script>

<template>
  <h1>Navbar</h1>

  <ShowToast
    v-if="visible"
    :toastTitle="toastTitle"
    :toastMessage="toastMessage"
    @click="visible=false"
  />
  <router-view/>
</template>
