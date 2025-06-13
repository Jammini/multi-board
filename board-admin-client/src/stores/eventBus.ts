import { reactive } from 'vue';

const eventBus = reactive({
  events: {} as Record<string, Function[]>,

  on(event: string, callback: Function) {
    if (!this.events[event]) {
      this.events[event] = [];
    }
    this.events[event].push(callback);
  },

  off(event: string, callback: Function) {
    if (!this.events[event]) return;
    const index = this.events[event].indexOf(callback);
    if (index > -1) {
      this.events[event].splice(index, 1);
    }
  },

  emit(event: string, data?: any) {
    if (!this.events[event]) return;
    this.events[event].forEach(callback => callback(data));
  }
});

export { eventBus };
