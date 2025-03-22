import mitt from 'mitt';

type Events = {
  login: void;
  logout: void;
};

export const eventBus = mitt<Events>();
