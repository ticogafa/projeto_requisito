
import { Outlet } from "react-router-dom";
import { ToastContainer } from 'react-toastify';
import { TheLoading } from "./components/common/TheLoading";
import { useLoadingStore } from "./store/useLoadingStore";

export default function App() {
  const { isLoading } = useLoadingStore();
  return (
    <div>
      {isLoading ? <TheLoading/> : null}
      <ToastContainer
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="dark"
      />
      <Outlet />
    </div>
  )
}
