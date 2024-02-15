import { useEffect, useState } from "react";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";
import { API } from "../config";
import { Grid, TextField, Typography, Button, FormControlLabel, Checkbox } from "@mui/material";
import {FormControl} from "@mui/material";

const CheckBoxes = ({sources, selectedSources, handleCheck}) => {
    const sourceValues = Object.values(sources);
    return sourceValues.map((source) => {
        return (
            <Grid key={source.id}  item xs={6} justifyItems={"start"}>
                <FormControlLabel
                    control={
                        <Checkbox 
                            id={source.id.toString()} 
                            checked={selectedSources[source.id]} 
                            onChange={handleCheck}
                        />
                    }
                    label={`${source.description}`}
                />
            </Grid>
        )
    })
}

const Edit = () => {
    const params = useParams();
    const email = params.email;
    const [searchParams] = useSearchParams();
    const name = searchParams.get('name');

    const [isLoaded, setIsLoaded] = useState(false);
    const [message, setMessage] = useState("Loading...");
    const [user, setUser] = useState({});

    const [sources, setSources] = useState({});
    const [selectedSources, setSelectedSources] = useState({});


    const navigate = useNavigate();

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

        const getOrigins = async () => {
            try {
                const response = await fetch(API.SOURCES, {
                    headers: {
                        "Content-Type": "application/json"
                    },
                    method: 'get'
                })
                const data = await response.json();
                setSources(data.sourceMap);
                const newObj = {};
                Object.values(data.sourceMap).map((source) => {
                    newObj[source.id] = false;
                })
                setSelectedSources(newObj);
            } catch (e) {
                console.err(e);
            }
        }
        getOrigins();
        getMember();
    }, [])

    const handleCheck = (e) => {
        const id = e.target.id;
        const newObj = {...selectedSources}
        if (e.target.checked) {
            newObj[id] = true;
            setSelectedSources(newObj);
        } else {
            newObj[id] = false;
            setSelectedSources(newObj);
        }
    }

    

    const handleFormSubmitted = async (e) => {
        e.preventDefault();
        // 공지사항 출처(Object) -> Array 형태로 변경
        const selectedArr = Object.keys(selectedSources).filter((id) => selectedSources[id] === true)
        console.log(selectedArr);
        
        const payload = {
            email: user.email,
            name: user.name,
            selectedSources: selectedArr
        }

        const response = await fetch(API.SUBSCRIPTIONS + `/${email}?name=${name}`, {
            headers: {
                "Content-Type": "application/json"
            },
            method: 'put',
            body: JSON.stringify(payload)
        })
        const data = await response.json();
        if (data.result === "success") {
            navigate(`/subscription/${email}?name=${name}`)
        }
    }

    return (
        isLoaded ?
        <>
            <Typography 
                variant="h4" 
                sx={{
                    fontWeight: "bold"
                }} 
                color="primary.main" 
                gutterBottom
            >
                {`${name}님의 구독 정보 수정`}
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
                    <Grid container justifyContent={"flex-start"} item >
                        {sources && <CheckBoxes sources={sources} selectedSources={selectedSources} handleCheck={handleCheck} />}
                    </Grid>
                    <Grid item xs={8}></Grid>
                    <Grid item xs={4}>
                        <Button
                            sx={{
                                height: "100%",
                                width: "100%"
                            }}
                            onClick={handleFormSubmitted}
                            variant="outlined"
                        >
                            신청하기
                        </Button>
                    </Grid>
                </Grid>
            </FormControl>
        </>
        // <Form onSubmit={handleFormSubmitted} noValidate>
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
        //     <Checkboxes origins={origins} handleChange={handleCheckboxChange}></Checkboxes>
        //     <Button onClick={handleFormSubmitted}>구독 수정</Button>
        // </Form>
        : <h1>{message}</h1>
    )
}

export default Edit;