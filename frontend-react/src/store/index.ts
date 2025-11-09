import { configureStore } from '@reduxjs/toolkit';
import appReducer from './reducers/appReducer';
import userReducer from './reducers/userReducer';

const store = configureStore({
  reducer: {
    app: appReducer,
    user: userReducer,
  },
});

export default store;

// Tipos (opcional mas recomendado)
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
