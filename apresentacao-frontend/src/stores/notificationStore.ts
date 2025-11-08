import { defineStore } from "pinia";
import { ref } from "vue";

export const useNotificationStore = defineStore('notification', () => {
  const toastTitle = ref('');
  const toastMessage = ref('');
  const visible = ref(false);

 function showToast(title: string, message: string) {
    toastTitle.value = title;
    toastMessage.value = message;

    visible.value = true;

    setTimeout(() => {
      visible.value = false;
    }, 4000);
  }

  return { showToast, toastTitle, toastMessage, visible };
});
