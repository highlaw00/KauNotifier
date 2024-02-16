import {
  Box,
  Card,
  CardContent,
  Stack,
  Typography,
  useMediaQuery,
} from "@mui/material";
import Carousel from "react-material-ui-carousel";
import { Button } from "@mui/material";
import { Link } from "react-router-dom";

const IntroCard = ({content}) => {
    const {header, title, body} = content;
    return (
        <Card variant="outlined" sx={{ flexGrow: 1, flexBasis: "33%" }}>
            <CardContent>
                <Typography sx={{ fontSize: "0.9rem" }} color="text.secondary" gutterBottom>
                    {header}
                </Typography>
                <Typography gutterBottom={true} variant="h5" component="div">
                    {title}
                </Typography>
                <Typography variant="body2">
                    {body}
                </Typography>
            </CardContent>
        </Card>
    )
}

const Home = () => {

    const isMobile = useMediaQuery("(max-width: 992px)");

    const cardContents = [
        {
            header: "Step 1.",
            title: "이메일 인증",
            body: <>공지사항을 받을 이메일을 인증하세요. <br/> 인증 코드는 입력한 이메일로 전송됩니다.</>
        },
        {
            header: "Step 2.",
            title: "구독",
            body: <>알람 받길 원하는 공지사항 종류를 구독하세요. <br/> 구독이 완료되면 <strong>매일 밤 9시</strong>에 공지사항 목록이 전송됩니다.</>
        },
        {
            header: "Step 3.",
            title: "구독 취소 및 수정",
            body: <>원하시는 공지사항이 변경되었나요? <br/>간편하게 구독을 수정하거나 취소할 수 있습니다.</>
        },
    ]

    const introCards = cardContents.map((elem, idx) => <IntroCard key={idx} content={elem}/>)

    const pages = [
        <Box sx={{
            minHeight: "90vh",
            display: "flex",
            flexDirection: "column",
            justifyContent: "center"
        }}
            key={"1"}
        >
            <Typography variant="body1" color="primary.main">이메일로 간편하게</Typography>
            <Typography variant="h4" sx={{fontWeight: "bold"}} color="primary.dark">
                한국항공대학교 <br/> 
                공지사항 구독 서비스
            </Typography>
        </Box>,
        <Box sx={{
            minHeight: "90vh",
            display: "flex",
            flexDirection: "column",
            justifyContent: "center"
        }}
            key={"2"}
        >
            <Typography variant="h4" gutterBottom={true} sx={{fontWeight: "bold"}} color="primary.dark">
                무슨 서비스인가요?
            </Typography>
            <Typography gutterBottom={true}>항공대학교 홈페이지에 업로드 되는 공지사항을 이메일로 받아볼 수 있는 구독 서비스입니다.</Typography>
            <Typography>매일 밤 9시에 그 날 공지사항이 포함된 이메일이 발송되며, 공지사항이 없는 날이면 발송되지 않습니다.</Typography>
        </Box>,
        <Box sx={{
            minHeight: "90vh",
            display: "flex",
            flexDirection: "column",
            justifyContent: "center"
        }} 
            key={"3"}
        >
            <Typography variant="h4" gutterBottom={true} sx={{fontWeight: "bold"}} color="primary.dark">
                어떻게 사용하나요?
            </Typography>
            <Stack direction={{md:"row"}} spacing={1}>
                {introCards}
            </Stack>
        </Box>,
        <Box sx={{
            minHeight: "90vh",
            display: "flex",
            flexDirection: "column",
            justifyContent: "center"
        }} 
            key={"4"}
        >
            <Typography variant="h4" gutterBottom={true} sx={{fontWeight: "bold"}} color="primary.dark">
                왜 만들었나요?
            </Typography>
            <Typography gutterBottom={true}>
                항공대 학생 분들이 중요한 공지사항을 확인하기 위해 매일 같이 홈페이지에 접속하는 번거로움을 줄이고자 만들었습니다.
            </Typography>
            <Typography>
                2024년 기준, 항공대학교는 카카오 워크 서비스를 사용하여 공지사항을 전송하고 있지만 학교 측에서 전송하는 공지사항만 받을 수 있어 보다 더 능동적인 서비스를 만들었습니다.
            </Typography>
        </Box>,
        <Box sx={{
            minHeight: "90vh",
            display: "flex",
            flexDirection: "column",
            justifyContent: "center"
        }} 
            key={"5"}
        >
            
            <Button color="info">
                <Link to={"/subscribe"} style={{textDecoration: "inherit", color: "inherit"}}>
                    <Typography variant="h3" sx={{fontWeight: "bold"}}>
                        시작하기
                    </Typography>
                </Link>
            </Button>
            
        </Box>
    ]
    
    return (
        <Carousel autoPlay={false} cycleNavigation={false}>
            {pages}
        </Carousel>
    )
}

export default Home;
