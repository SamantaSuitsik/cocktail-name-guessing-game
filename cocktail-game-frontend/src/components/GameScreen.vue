<template>
  <v-container>
    <v-row align="center" justify="center">
      <v-col cols="auto">
        <v-card class="pa-8" color="indigo-accent-1" width="120vh">
          <v-row align="center" justify="center" no-gutters >
            <v-col cols="auto">
              <v-icon aria-hidden="false" color="orange-lighten-2" size="71">
                mdi-star
              </v-icon>
            </v-col>
            <v-col cols="auto">
              <p class="text-h3 font-weight-medium text-black" :class="scoreAnimation">
                {{ score }}
              </p>
            </v-col>
            <v-col cols="auto">
              <v-icon aria-hidden="false" color="pink-accent-1" size="69">
                mdi-heart
              </v-icon>
            </v-col>
            <v-col cols="auto">
              <p class="text-h3 font-weight-medium text-black" :class="attemptsAnimation">
                {{ attemptsLeft }}
              </p>
            </v-col>
          </v-row>
            <v-row align="center" justify="center">
              <v-col cols="9" class="text-black">
                <v-card class="facts pa-3" color="indigo-accent-2">
                  <p><strong>Instructions: </strong> {{ instructions }}</p>
                  <ul style="list-style-type: none;">
                    <li v-for="fact in facts">
                      <v-row align="center" justify="center">
                        <v-col cols="auto">
                          <p v-show="!isImgUrl(fact)">
                            {{fact}}
                          </p>
                          <v-img v-show="isImgUrl(fact)" :src="fact" width="300"></v-img>
                        </v-col>
                      </v-row>
                    </li>
                  </ul>
                </v-card>
                <br>
              </v-col>
            </v-row>
          <v-row align="center" justify="center">
            <v-col cols="auto">
              <v-card-text class="text-h4 text-black">
                {{dashedName}}
              </v-card-text>
            </v-col>
          </v-row>
          <v-row justify="center" align="center">
            <v-text-field v-model="userGuess" label="Your guess" class="text- text-black" variant="outlined" style="max-width: 60%"/>
          </v-row>
          <v-row align="center" justify="center">
            <v-row align="center" justify="space-between" style="max-width: 60%">
              <v-btn @click="onSubmit(userGuess)">Submit</v-btn>
              <v-btn @click="skipRound()">Skip</v-btn>
            </v-row>
          </v-row>
          <v-card-text>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
  <v-dialog v-model="gameLost">
    <v-row align="center" justify="center">
      <v-col cols="auto">
        <v-card class="pa-5" color="pink-darken-2">
          <h3>Game over! You have used all of your attempts!
            <br>
            Actual name was: {{ cocktailName }}
            <br>
            Final score: {{score}}
          </h3>
          <v-btn class="mt-4" @click="restartGame()">
            Play again
          </v-btn>
        </v-card>
      </v-col>
    </v-row>
  </v-dialog>
</template>

<script setup>
import { ref } from "vue";
import { useGameStore } from "@/stores/gameStore";

const userGuess = ref('');

const gameStore = useGameStore();
const cocktailName = computed(() => gameStore.getCocktailName);
const dashedName = computed(() => gameStore.gethiddenLetters);
const score = computed(() => gameStore.getScore);
const attemptsLeft = computed(() => gameStore.getAttemptsLeft);
const instructions = computed(() => gameStore.getInstructions);
const facts = computed(() => gameStore.getFacts);
const gameLost = computed(() => gameStore.getGameLost);

const scoreAnimation = ref('');
const attemptsAnimation = ref('');

watch(score, () => {
  scoreAnimation.value = 'score-change';
  setTimeout(() => {
    scoreAnimation.value = '';
  }, 500);
});

watch(attemptsLeft, () => {
  attemptsAnimation.value = 'attempt-change';
  setTimeout(() => {
    attemptsAnimation.value = '';
  }, 500);
});


function isImgUrl(fact) {
  try {
    return Boolean(new URL(fact));
  }
  catch (e) {
    return false;
  }
}

function onSubmit(guess = " ") {
  gameStore.guess(guess);
  userGuess.value = '';
}

function skipRound() {
  gameStore.skipRound();
}

function restartGame() {
  gameStore.startGame();
}

onMounted(() => {
  gameStore.startGame()
});
</script>

<style scoped>
.score-change {
  animation: scoreChange 0.5s ease-in-out;
}

@keyframes scoreChange {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.5);
  }
  100% {
    transform: scale(1);

  }
}

.attempt-change {
  animation: attemptChange 0.5s ease-in-out;
}

@keyframes attemptChange {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.5);
  }
  100% {
    transform: scale(1);
  }
}

</style>
