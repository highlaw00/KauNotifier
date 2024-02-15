import { useEffect, useState } from "react";
import { useLocation, useParams, useSearchParams } from "react-router-dom";
import { API } from "../config";
import { useNavigate } from "react-router-dom";
import { Divider, FormControl, Grid, List, ListItem, TextField, Typography, Button, Modal, Box } from "@mui/material";

const Subscription = () => {
    const params = useParams();
    const email = params.email;
    const [searchParams] = useSearchParams();
    const name = searchParams.get('name');

    const [isLoaded, setIsLoaded] = useState(false);
    const [message, setMessage] = useState("Loading...");
    const [user, setUser] = useState({});
    const [isOpen, setIsOpen] = useState(false);

    const modalStyle = {
        position: 'absolute',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        width: 400,
        bgcolor: 'background.paper',
        border: '2px solid #000',
        boxShadow: 24,
        p: 4,
      };

    const navigate = useNavigate();

    // 회원 정보 가져오기
    useEffect(() => {
        const getMember = async () => {
            try {
                const response = await fetch(API.SUBSCRIPTIONS + `/${email}?name=${name}`, {
                    headers: {
                        "Content-Type": "application/json"
                    },
                    method: 'get',
                })
                const data = await response.json();
                if (data.result === "success") {
                    setUser(data);
                    setIsLoaded(true);
                    console.log(data);
                } else {
                    setMessage(data.message)
                }
            } catch (e) {
                setIsLoaded(true);
                setMessage("잘못된 요청입니다.");
            }
            
        };
        getMember();
    }, [])

    const openModal = () => setIsOpen(true);
    const closeModal = () => setIsOpen(false);

    const handleModify = () => {
        navigate(`/subscription/${email}/edit?name=${name}`)
    }

    const handleDeletion = async () => {
        const payload = {
            email: email,
            name: name,
        }
        const response = await fetch(API.SUBSCRIPTIONS + `/${email}`, {
            headers: {
                "Content-Type": "application/json"
            },
            method: 'delete',
            body: JSON.stringify(payload)
        })
        const data = await response.json();
        console.log(data);
        if (data.result === "success") return navigate("/");
    }

    const OriginList = ({user}) => {
        const sources = user.subscribingSources;
        const elements = sources.map((elem, idx) => {
            return (
                <Grid item xs={12}>
                    <TextField 
                        key={elem.id} 
                        aria-readonly="true" 
                        value={`${idx + 1}. ${elem.description}`}
                        fullWidth 
                    />
                </Grid>
            )
        });
        return (
            <>
                {elements}
            </>
        )
    }

    return (
        isLoaded ? 
        <>
            <Typography variant="h4" sx={{fontWeight: "bold"}} color="primary.main" gutterBottom>
                {`${name}님의 구독 정보`}
            </Typography>
            <FormControl sx={{minWidth: "100%"}}>
                <Grid container justifyContent="center" alignItems="center" spacing={2}>
                    <Grid item xs={2}>
                        <Typography>이메일</Typography>
                    </Grid>
                    <Grid item xs={10}>
                        <TextField
                            id="email-field" 
                            fullWidth
                            value={email}
                            aria-readonly="true"
                            type="email"
                        />
                    </Grid>
                    <Grid item xs={2}>
                        <Typography>이름</Typography>
                    </Grid>
                    <Grid item xs={10}>
                        <TextField
                            id="text-field" 
                            fullWidth
                            value={name}
                            aria-readonly="true"
                            type="text"
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <Divider textAlign="left">
                            <Typography>구독 중인 공지사항</Typography>
                        </Divider>
                    </Grid>
                    <Grid container item xs={12} spacing={1}>
                        {<OriginList user={user}/>}
                    </Grid>
                    <Grid item xs={7}>
                    </Grid>
                    <Grid container justifyContent={"end"} item xs={5}>
                        <Button 
                            variant="outlined"
                            sx={{
                                flexGrow: 1, maxWidth: "300px"
                            }}
                            onClick={handleModify}
                        >
                            수정
                        </Button>
                        <Button 
                            variant="outlined" 
                            color="error" 
                            sx={{
                                flexGrow: 1, maxWidth: "300px", ml: 1
                            }}
                            onClick={openModal}
                        >
                            삭제
                        </Button>
                        <Modal
                            open={isOpen}
                            onClose={closeModal}
                        >
                            <Grid container sx={modalStyle}>
                                <Grid item xs={12}>
                                    <Typography id="modal-modal-title" variant="h6" component="h2" gutterBottom>
                                        정말 구독을 취소하시겠습니까?
                                    </Typography>
                                </Grid>
                                <Grid item xs={6}></Grid>
                                <Grid container item xs={6} justifyContent={"end"}>
                                    <Button variant="outlined" onClick={handleDeletion}>
                                        예
                                    </Button>
                                    <Button variant="outlined" sx={{ml: 1}} onClick={closeModal}>
                                        아니오
                                    </Button>
                                </Grid>
                            </Grid>
                        </Modal>
                    </Grid>
                </Grid>
            </FormControl>
        </>
        // <Form onSubmit={(e) => e.preventDefault()} noValidate>
        //     <Form.Label htmlFor="inputEmail">이메일 주소</Form.Label>
        //     <Form.Control
        //         id="inputEmail"
        //         value={user.email}
        //         required
        //         readOnly
        //         type="email"
        //         className="mb-2"
        //     />
        //     <Form.Label htmlFor="inputName">이름</Form.Label>
        //     <Form.Control
        //         id="inputName"
        //         value={user.name}
        //         required
        //         readOnly
        //         type="text"
        //         className="mb-2"
        //         />
        //     {/* <Checkboxes origins={origins} handleChange={handleCheckboxChange}></Checkboxes> */}
        //     {/* <Row>
        //         <Col><Button onClick={handleModify}>수정</Button></Col>
        //         <Col><Button onClick={handleDeletion}>삭제</Button></Col>
        //     </Row> */}
        //     <OriginList user={user}></OriginList>
        //     <Button onClick={handleModify}>수정</Button>
        //     <Button onClick={handleDeletion}>삭제</Button>
        // </Form>
        : <h1>{message}</h1>
    )
    // const { email, name } = data;
    // const [origins, setOrigins] = useState({});
    // const [selectedOrigins, setSelectedOrigins] = useState([]);

    // // 공지사항 출처 불러오기
    // useEffect(() => {
    //     async function fetchOrigins() {
    //         // Get Origins and Update.
    //         const response = await fetch(API.SOURCES, {
    //             headers: {
    //                 "Content-Type": "application/json"
    //             },
    //             method: 'get'
    //         })
    //         const originsData = await response.json();
    //         setOrigins(originsData.sourceMap);
    //     };
    //     fetchOrigins();
    // }, [])


    // // 체크박스 클릭시 selectedSources 변경
    // const handleCheckboxChange = (e) => {
    //     const selectedOriginId = e.target.value;
    //     const isChecked = e.target.checked;
    //     // 선택한 경우
    //     if (isChecked) {
    //         setSelectedOrigins(prevOrigins => [...prevOrigins, selectedOriginId])
    //     }
    //     // 선택해제한 경우 
    //     else {
    //         setSelectedOrigins(prevOrigins => prevOrigins.filter((originId) => originId !== selectedOriginId));
    //     }
    // }

    // const handleModify = async () => {
    //     const payload = {
    //         email: email,
    //         name: name,
    //         selectedSources: selectedOrigins
    //     }
    //     const response = await fetch(API.SUBSCRIPTIONS + `/${email}`, {
    //         headers: {
    //             "Content-Type": "application/json"
    //         },
    //         method: 'put',
    //         body: JSON.stringify(payload)
    //     })
    //     const data = await response.json();
    //     console.log(data);
    // }
    // const handleDeletion = async () => {
    //     const payload = {
    //         email: email,
    //         name: name,
    //     }
    //     const response = await fetch(API.SUBSCRIPTIONS + `/${email}`, {
    //         headers: {
    //             "Content-Type": "application/json"
    //         },
    //         method: 'delete',
    //         body: JSON.stringify(payload)
    //     })
    //     const data = await response.json();
    //     console.log(data);
    //     if (data.result === "success") return redirect("/");
    // }

    // return (
    // <Form noValidate>
    //     <InputGroup className="mb-2">
    //         <Form.Control
    //             value={email}
    //             required
    //             readOnly
    //             type="email"
    //         />
    //     </InputGroup>
    //     <Form.Control
    //         value={name}
    //         required
    //         readOnly
    //         type="text"
    //         className="mb-2"
    //         />
    //     <Checkboxes origins={origins} handleChange={handleCheckboxChange}></Checkboxes>
    //     <Row>
    //         <Col><Button onClick={handleModify}>수정</Button></Col>
    //         <Col><Button onClick={handleDeletion}>삭제</Button></Col>
    //     </Row>
    // </Form>
    // );
}

export default Subscription;