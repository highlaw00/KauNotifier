import { AppBar, Box, Toolbar, Typography, IconButton, SwipeableDrawer, List, ListItem, ListItemButton, ListItemIcon, ListItemText, Container, useMediaQuery } from '@mui/material';
import { Home, Mail, Menu, FindInPage } from '@mui/icons-material';
import { useState } from 'react';
import { Link, Outlet } from 'react-router-dom';

const Root = () => {
  const isMobile = useMediaQuery("(max-width: 767px)");

  const [isOpen, setIsOpen] = useState(false);
  const toggleDrawer = (open) => (e) => {
    if (
      e && 
      e.type === 'keydown' &&
      (e.key === 'Tab' || e.key === 'Shift')
    ) {
      return;
    }

    setIsOpen(open)
  }

  const drawer = () => {
    return (
      <Box
        sx={{ width: isMobile ? "200px" : "350px" }}
        role="presentation"
        onClick={toggleDrawer(false)}
        onKeyDown={toggleDrawer(false)}
      >
        <List>
          <ListItem disablePadding>
            <ListItemButton>
              <ListItemIcon>
                <Home/>
              </ListItemIcon>
              <Link to={"/"} style={{
                color: "inherit",
                textDecoration: "inherit",
                width: "100%"
              }}>
              <ListItemText primary={"홈"}/>
              </Link>
            </ListItemButton>
          </ListItem>
          <ListItem disablePadding>
            <ListItemButton>
              <ListItemIcon>
                <Mail/>
              </ListItemIcon>
              <Link to={"/subscribe"} style={{
                color: "inherit",
                textDecoration: "inherit",
                width: "100%"
              }}> 
                <ListItemText primary={"구독 신청"}/>
              </Link>
            </ListItemButton>
          </ListItem>
          <ListItem disablePadding>
            <ListItemButton>
              <ListItemIcon>
                <FindInPage/>
              </ListItemIcon>
              <Link to={"/subscriptions"} style={{
                color: "inherit",
                textDecoration: "inherit",
                width: "100%"
              }}> 
                <ListItemText primary={"구독 조회"}/>
              </Link>
            </ListItemButton>
          </ListItem>
        </List>
      </Box>
    )
  }

    return (
      <>
      <Box sx={{ flexGrow: 1 }}>
        <AppBar position="static" sx={{
          bgcolor: "primary.extremeLight"
        }}>
          <Toolbar>
            <Link to={"/"} style={{textDecoration: "inherit", color: "inherit", flexGrow: 1}}>
              <Typography variant="h6" component="div" color={"primary.dark"}>
                  Kau Notifier
              </Typography>
            </Link>
            <IconButton
              size="large"
              color="primary.dark"
              edge="end"
              aria-label="menu"
              onClick={toggleDrawer(true)}
            >
              <Menu />
            </IconButton>
            <SwipeableDrawer
              anchor='right'
              open={isOpen}
              onClose={toggleDrawer(false)}
              onOpen={toggleDrawer(true)}
            >
              {drawer()}
            </SwipeableDrawer>
            
          </Toolbar>
      </AppBar>
    </Box>
    <Container sx={{
      minHeight: "95vh",
      paddingTop: "16px"
    }}>
        <Outlet />
    </Container>
    </>
    );
}

export default Root;