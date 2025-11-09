import { BrowserRouter, Route, Routes } from 'react-router-dom';
import ClientNavigator from '../navigators/ClientNavigator';

export default function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/client/*" element={<ClientNavigator />} />
      </Routes>
    </BrowserRouter>
  );
}
