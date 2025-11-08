
<script setup lang="ts">

const selectProfile = (profileType: string) => {
  console.log(`Perfil selecionado: ${profileType}`);
};

</script>

<template>
      <div
        id="profileSelection"
        class="min-h-screen flex flex-col items-center justify-center p-8"
      >
        <div class="text-center mb-12">
          <div class="flex items-center justify-center gap-3 mb-4">
            <span class="material-icons text-primary text-6xl"
              >content_cut</span
            >
            <h1 class="text-5xl font-bold text-primary">Sistema Barbearia</h1>
          </div>
          <p class="text-gray-400 text-xl">
            Selecione o tipo de acesso para começar
          </p>
        </div>

        <div class="grid grid-cols-3 gap-8 max-w-6xl w-full">
          <div
            @click="selectProfile('admin')"
            class="profile-card bg-dark-800 rounded-2xl p-8 cursor-pointer border-2 border-dark-600 hover:border-primary"
          >
            <div class="text-center">
              <div
                class="bg-primary/10 w-32 h-32 rounded-full flex items-center justify-center mx-auto mb-6"
              >
                <span
                  class="material-icons text-primary"
                  style="font-size: 72px"
                  >admin_panel_settings</span
                >
              </div>
              <h3 class="text-2xl font-semibold mb-2">Administrador</h3>
              <p class="text-gray-400">Gerencie toda a operação da barbearia</p>
            </div>
          </div>

          <div
            @click="selectProfile('professional')"
            class="profile-card bg-dark-800 rounded-2xl p-8 cursor-pointer border-2 border-dark-600 hover:border-primary"
          >
            <div class="text-center">
              <div
                class="bg-primary/10 w-32 h-32 rounded-full flex items-center justify-center mx-auto mb-6"
              >
                <span
                  class="material-icons text-primary"
                  style="font-size: 72px"
                  >badge</span
                >
              </div>
              <h3 class="text-2xl font-semibold mb-2">Profissional</h3>
              <p class="text-gray-400">Realize e gerencie seus atendimentos</p>
            </div>
          </div>

          <div
            @click="selectProfile('client')"
            class="profile-card bg-dark-800 rounded-2xl p-8 cursor-pointer border-2 border-dark-600 hover:border-primary"
          >
            <div class="text-center">
              <div
                class="bg-primary/10 w-32 h-32 rounded-full flex items-center justify-center mx-auto mb-6"
              >
                <span
                  class="material-icons text-primary"
                  style="font-size: 72px"
                  >person</span
                >
              </div>
              <h3 class="text-2xl font-semibold mb-2">Cliente</h3>
              <p class="text-gray-400">Agende e avalie seus atendimentos</p>
            </div>
          </div>
        </div>
      </div>

</template>

<style lang="">

</style>
