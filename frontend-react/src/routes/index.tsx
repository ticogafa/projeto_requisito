import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Pay from '../views/Pay/Pay';


export default function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Pay />} />
      </Routes>
    </BrowserRouter>
  );
}
