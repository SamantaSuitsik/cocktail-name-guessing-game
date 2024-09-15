import {defineStore} from 'pinia';
import axios from '../api/axios';

export const useGameStore = defineStore('gameStore', {
  state: () => ({
    score: 0,
    attemptsLeft: 0,
    gameLost: false,
    cocktailName: '',
    hiddenLetters: '',
    instructions: '',
    facts: [],
  }),
  getters: {
    getScore(state) {
      return state.score;
    },
    getAttemptsLeft(state) {
      return state.attemptsLeft;
    },
    getCocktailName(state) {
      return state.cocktailName;
    },
    gethiddenLetters(state) {
      return state.hiddenLetters;
    },
    getInstructions(state) {
      return state.instructions;
    },
    getFacts(state) {
      return state.facts;
    },
    getGameLost(state) {
      return state.gameLost;
    },
  },
  actions: {
    async startGame() {
      try {
        const response = await axios.post('/start');
        const data = response.data
        this.cocktailName = data.cocktailName;
        this.hiddenLetters = data.hiddenLetters;
        this.score = data.score;
        this.attemptsLeft = data.attemptsLeft;
        this.instructions = data.instructions;
        this.facts = data.facts;
        this.gameLost = data.gameLost;
      } catch (error) {
          console.error('Error fetching game state:', error);
      }
    },
    async guess(payload){
      try {
          const response = await axios.post('/guess', payload, {
            headers: {
              'Content-Type': 'text/plain',
            },
          });
          const data = response.data;
          this.cocktailName = data.cocktailName;
          this.hiddenLetters = data.hiddenLetters;
          this.score = data.score;
          this.attemptsLeft = data.attemptsLeft;
          this.instructions = data.instructions;
          this.facts = data.facts;
          this.gameLost = data.gameLost;
      } catch (e) {
          console.error('Error with guessing:', e);
      }
    },
    async skipRound(){
      try {
        const response = await axios.post("/skip")
          const data = response.data
          this.cocktailName = data.cocktailName;
          this.hiddenLetters = data.hiddenLetters;
          this.score = data.score;
          this.attemptsLeft = data.attemptsLeft;
          this.instructions = data.instructions;
          this.facts = data.facts;
          this.gameLost = data.gameLost;
      } catch (e) {
          console.error('Error with skipping round:', e);
      }
    }
  },
});
