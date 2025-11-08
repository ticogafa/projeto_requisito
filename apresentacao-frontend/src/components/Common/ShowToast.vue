<script setup lang="ts">
defineProps<{
  toastTitle: string;
  toastMessage: string;
}>();

</script>

<template>
   <div
        style="z-index: 999;"
        class="fixed top-8 right-8 bg-green-500/90 text-white border-primary rounded-lg p-4 shadow-2xl z-50 max-w-sm"
      >
        <div class="flex items-center gap-3">
          <div>
            <p class="font-semibold" id="toastTitle">{{ toastTitle }}</p>
            <p class="text-sm text-white-400" id="toastMessage">
              {{ toastMessage }}
            </p>
          </div>
        </div>
      </div>
</template>
