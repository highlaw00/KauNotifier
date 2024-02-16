import logo from './logo.svg';
import './App.css';
import Root from './fragments/Root';
import Home from './pages/home';
import View from './pages/view';
import ErrorPage from './pages/error';
import Subscription from './components/Subscription';
import Edit from './components/Edit';
import { createTheme, ThemeProvider } from '@mui/material';
import Subscribe from './pages/Subscribe'
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import Test from './pages/Test';

const theme = createTheme({
  typography: {
    fontFamily: "'Noto Serif KR'"
  },
  palette: {
    primary: {
      extremeLight: '#e1f5fe',
      light: '#4FC3F7',
      main: '#03A9F4',
      dark: '#0476bd'
    },
    on: {

    }
  }
})

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root></Root>,
    errorElement: <ErrorPage></ErrorPage>,
    children: [
      {
        path: "/",
        element: <Home></Home>
      },
      {
        path: "subscribe",
        element: <Subscribe></Subscribe>
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
    ]
  }
]);

function App() {
  return (
    <div>
      <ThemeProvider theme={theme}>
        <RouterProvider router={router}/>
      </ThemeProvider>
    </div>
  );
}

export default App;
