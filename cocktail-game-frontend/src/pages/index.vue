<template>
  <v-icon @click="toggleSound()" size="50" style="position: absolute;" class="ma-2">
    mdi-volume-high
  </v-icon>
  <Home v-if="!isGameStarted" @start-game="startGame()"></Home>
  <GameScreen v-if="isGameStarted"></GameScreen>
</template>

<script setup>
import Home from "@/components/Home.vue";
import { ref } from "vue";
import GameScreen from "@/components/GameScreen.vue";

const isGameStarted = ref(false);
const audio = new Audio('/cookie.mp3');
audio.loop = true;

function toggleSound() {
  if (audio.paused) {
    audio.play();
  }
  else {
    audio.pause();
  }
}

function startGame() {
  isGameStarted.value = true;
}

</script>

<style>
  .v-main {
    background-color: #fbe1e1;
    background-image: url('@/assets/cocktail.png') !important;
    background-repeat: no-repeat !important;
    background-position: left bottom !important;
    background-size: 20rem !important;

  }

</style>
