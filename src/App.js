import logo from './logo.svg';
import './App.css';
import Header from './fragments/header';
import Footer from './fragments/footer';
import Home from './pages/home';
import Subscribe from './pages/subscribe';
import View from './pages/view';
import ErrorPage from './pages/error';
import Subscription from './components/Subscription';
import Edit from './components/Edit';
import { createTheme, ThemeProvider } from '@mui/material';
import SubscribeTest from './pages/SubscribeTest'



import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import Container from 'react-bootstrap/esm/Container';
import Test from './pages/Test';

const theme = createTheme({
  typography: {
    fontFamily: "'Noto Serif KR'"
  },
  palette: {
    primary: {
      light: '#4FC3F7',
      main: '#03A9F4',
      dark: '#01579B'
    },
    on: {

    }
  }
})

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
    path: "subscribe/test",
    element: <SubscribeTest></SubscribeTest>
  },
  {
    path: "subscriptions",
    element: <View></View>
  },
  {
    path: "subscription/:email",
    element: <Subscription></Subscription>
  },
  {
    path: "subscription/:email/edit",
    element: <Edit></Edit>
  },
  {
    path: "test",
    element: <Test></Test>
  }
]);

function App() {
  return (
    <div>
      <ThemeProvider theme={theme}>
      <Header/>
      <Container>
        <RouterProvider router={router}/>
      </Container>
      </ThemeProvider>
    </div>
  );
}

export default App;
