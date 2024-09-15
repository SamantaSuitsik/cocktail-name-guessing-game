<template>

  <v-container width="42%">
    <v-row align="center" justify="center">
      <v-col cols="auto">
        <v-card class="pa-3" color="indigo-accent-1">
          <v-card-text class="text-h4 text-black font-weight-bold">
            Welcome to Guess the cocktail!
          </v-card-text>
          <v-card-text class="text-h6 text-black">
            The aim of this game is to guess the cocktail's name by the number of letters shown and additional facts about the cocktail.
          </v-card-text>
          <v-row justify="center" class="my-3">
            <v-btn class="px-6" @click="$emit('start-game')">
              Start
            </v-btn>
          </v-row>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
defineEmits(['start-game'])
</script>

<style scoped>

</style>
