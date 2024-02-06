import logo from './logo.svg';
import './App.css';
import Header from './fragments/header';
import Footer from './fragments/footer';
import Home from './pages/home';
import Subscribe from './pages/subscribe';
import View from './pages/view';
import ErrorPage from './pages/error';

import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import Container from 'react-bootstrap/esm/Container';

const router = createBrowserRouter([
  {
    path: "/",
    element: <Home></Home>,
    errorElement: <ErrorPage></ErrorPage>
  },
  {
    path: "subscribe",
    element: <Subscribe></Subscribe>
  },
  {
    path: "find",
    element: <View></View>
  }
]);

function App() {
  return (
    <div>
      <Header/>
        <Container>
        <RouterProvider router={router}/>
        </Container>
      <Footer/>
    </div>
  );
}

export default App;
