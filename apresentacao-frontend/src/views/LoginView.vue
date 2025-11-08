<script setup lang="ts">

const login = () => {}
const backToProfileSelection = () => {}

</script>

<template>
<div
        id="loginScreen"
        class="min-h-screen flex items-center justify-center p-8"
      >
        <div
          class="bg-dark-800 rounded-2xl p-10 w-full max-w-md border border-dark-600"
        >
          <div class="text-center mb-8">
            <span class="material-icons text-primary text-5xl mb-4"
              >content_cut</span
            >
            <h2 class="text-3xl font-bold mb-2">Sistema Barbearia</h2>
            <p class="text-gray-400" id="loginSubtitle">Entrando como...</p>
          </div>

          <form @submit="login" class="space-y-6">
            <div>
              <label class="block text-sm font-medium mb-2"
                >E-mail ou Usuário</label
              >
              <input
                type="text"
                class="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 focus:outline-none focus:border-primary"
                placeholder="Digite seu e-mail"
              />
            </div>

            <div>
              <label class="block text-sm font-medium mb-2">Senha</label>
              <input
                type="password"
                class="w-full bg-dark-700 border border-dark-600 rounded-lg px-4 py-3 focus:outline-none focus:border-primary"
                placeholder="Digite sua senha"
              />
            </div>

            <button
              type="submit"
              class="w-full bg-primary hover:bg-orange-600 text-white font-semibold py-3 rounded-lg transition"
            >
              Entrar
            </button>

            <div class="text-center">
              <a
                data-toast-title="Em breve"
                data-toast-message="Funcionalidade de recuperação de senha em desenvolvimento."
                data-toast-icon="info"
                href="#"
                class="text-primary hover:underline text-sm cursor-pointer"
                >Esqueceu sua senha?</a
              >
            </div>

            <div class="text-center pt-4 border-t border-dark-600">
              <button
                type="button"
                @click="backToProfileSelection"
                class="text-gray-400 hover:text-gray-200 text-sm"
              >
                ← Voltar para seleção de perfil
              </button>
            </div>
          </form>
        </div>
      </div>
</template>
