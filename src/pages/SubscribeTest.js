import { Box, TextField, Typography, FormControl, Grid, Button, useMediaQuery, FormControlLabel, FormGroup, Checkbox } from "@mui/material";
import { CheckBox, Send, EmailRounded, EmailOutlined } from "@mui/icons-material";
import { useEffect, useState } from "react";
import { API } from "../config";
import { useNavigate } from "react-router-dom";

const fetchAndSetSources = async (setSources, setSelectedSources) => {
    const response = await fetch(API.SOURCES, {
        headers: {
            "Content-Type": "application/json"
        },
        method: "get"
    })

    const data = await response.json();
    setSources(data.sourceMap);

    const newObj = {};
    Object.values(data.sourceMap).map((source) => {
        newObj[source.id] = false;
    })
    setSelectedSources(newObj);
}

const isViolatedEmail = (email) => {
    const re = /\S+@\S+\.\S+/;
    return !re.test(email);
}

const isViolatedName = (name) => {
    const re = /^[a-zA-Z가-힣\s]+$/;
    return !re.test(name);
}

const isViolatedCode = (code) => {
    const re = /^\d{6}$/;
    return !re.test(code);
}

const isEmptySources = (sources) => {
    let result = false;
    Object.keys(sources).forEach((existence) => {
        if (!existence) {
            result = true;
            return false;
        }
    })
    return result;
}

const CheckBoxes = ({sources, selectedSources, handleCheck}) => {
    const sourceValues = Object.values(sources);
    return sourceValues.map((source) => {
        return (
            <Grid key={source.id} item xs={6} justifyItems={"start"}>
                <FormControlLabel
                    control={
                    <Checkbox id={source.id.toString()} checked={selectedSources[source.id]} onChange={handleCheck}/>
                    }
                    label={`${source.description}`}
                />
            </Grid>
        )
    })
}

const SubscribeTest = () => {
    const isMobile = useMediaQuery("(max-width: 767px)")
    const navigate = useNavigate();

    const [sources, setSources] = useState({});
    const [selectedSources, setSelectedSources] = useState({});
    const [formState, setFormState] = useState({
        emailState: {
            value: "",
            isViolated: false,
            helpText: "실제로 사용하는 이메일을 입력해주세요.",
            errorText: "올바르지 않은 이메일 형식입니다."
        },
        nameState: {
            value: "",
            isViolated: false,
            helpText: "이름은 식별을 위해서만 사용되며, 실제 이름이 아니어도 됩니다.",
            errorText: "이름은 한글과 영어로만 입력이 가능합니다."
        },
        codeState: {
            value: "",
            isViolated: false,
            helpText: "이메일에 전송된 인증 코드를 입력해주세요.",
            errorText: "인증 코드 오류"
        },
    })
    const [isSent, setIsSent] = useState(false);

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

    const handleStateChange = (e) => {
        const id = e.target.id;
        setFormState((prev) => {
            const newValue = e.target.value;
            let newState, keyName;
            if (id.startsWith("email")) {
                newState = prev.emailState;
                keyName = "emailState";
            } else if (id.startsWith("name")) {
                newState = prev.nameState;
                keyName = "nameState";
            } else if (id.startsWith("code")) {
                newState = prev.codeState;
                keyName = "codeState";
            }
            newState.value = newValue;
            return {
                ...prev,
                [keyName]: newState
            }
        })
    }

    const sendCode = async () => {
        const isViolated = isViolatedEmail(formState.emailState.value);
        setFormState((prev) => {
            const newObj = { ...prev }
            newObj.emailState.isViolated = isViolated;
            return newObj;
        })
        if (isViolated) return;
        
        const email = formState.emailState.value;
        const response = await fetch(API.SEND_MAIL, {
            headers: {
                "Content-Type": "application/json"
            },
            method: "post",
            body: JSON.stringify({
                email: email
            })
        })

        const data = await response.json();

        console.log(data);

        if (data.result === "success") setIsSent(true);
        else alert(`인증 코드 전송에 실패하였습니다. \n${data.message}`)
    }

    const sendForm = async () => {
        const email = formState.emailState.value;
        const name = formState.nameState.value;
        const code = formState.codeState.value;

        const isEmailViolated = isViolatedEmail(email);
        const isNameViolated = isViolatedName(name);
        const isCodeViolated = isViolatedCode(code);
        const isEmpty = isEmptySources(selectedSources);

        setFormState((prev) => {
            const newObj = { ...prev }
            newObj.emailState.isViolated = isEmailViolated;
            newObj.nameState.isViolated = isNameViolated;
            newObj.codeState.isViolated = isCodeViolated;
            return newObj;
        })

        if (isEmailViolated || isNameViolated || isCodeViolated || isEmpty) return;

        // 공지사항 출처(Object) -> Array 형태로 변경
        const selectedArr = Object.keys(selectedSources).filter((id) => selectedSources[id] === true)
        console.log(selectedArr);
        
        const response = await fetch(API.SUBSCRIBE, {
            headers: {
                "Content-Type": "application/json"
            },
            method: "post",
            body: JSON.stringify({
                email: email,
                name: name,
                code: code,
                selectedSources: selectedArr
            })
        })

        const data = await response.json();
        if (data.result === "success") {
            navigate(`/subscription/${email}?name=${name}`)
        }
    }

    // 공지사항 출처 가져오기
    useEffect(() => {
        fetchAndSetSources(setSources, setSelectedSources);
    }, [])

    return (
        <>
            <Typography variant="h4" sx={{fontWeight: "bold"}} color="primary.main" gutterBottom>구독 신청</Typography>
            <FormControl sx={{minWidth: "100%"}}>
                <Grid container justifyContent="center" alignItems="center" spacing={2}>
                    <Grid item xs={8}>
                        <TextField 
                            id="email-field" 
                            fullWidth label="이메일"
                            error={formState.emailState.isViolated}
                            helperText={formState.emailState.isViolated ? formState.emailState.errorText : formState.emailState.helpText} 
                            value={formState.emailState.value} 
                            onChange={handleStateChange} 
                            type="email"
                        >
                            이메일
                        </TextField>
                    </Grid>
                    <Grid item xs={4} paddingBottom={3}>
                        {/* PC 환경 & 전송 되지 않은 경우에만 아이콘 노출 */}
                        <Button 
                            endIcon={!isSent && !isMobile ? <EmailOutlined/> : null} 
                            sx={{
                                height: "100%",
                                width: "100%"
                            }}
                            onClick={sendCode}
                            disabled={isSent}
                            variant="outlined"
                        >
                            {isSent ? "전송 완료" : "코드 전송"}
                        </Button>
                    </Grid>
                    <Grid item xs={12}>
                        <TextField 
                            id="code-field" 
                            fullWidth label="인증 코드" 
                            error={formState.codeState.isViolated}
                            helperText={formState.codeState.isViolated ? formState.codeState.errorText : formState.codeState.helpText} 
                            value={formState.codeState.value} 
                            onChange={handleStateChange}
                        >
                            인증 코드
                        </TextField>
                    </Grid>
                    <Grid item xs={12}>
                        <TextField 
                            id="name-field" 
                            fullWidth label="이름" 
                            error={formState.nameState.isViolated}
                            helperText={formState.nameState.isViolated ? formState.nameState.errorText : formState.nameState.helpText} 
                            value={formState.nameState.value} 
                            onChange={handleStateChange}
                        >
                            이름
                        </TextField>
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
                            onClick={sendForm}
                            variant="outlined"
                        >
                            신청하기
                        </Button>
                    </Grid>
                </Grid>
            </FormControl>
        </>
    )
}

export default SubscribeTest;