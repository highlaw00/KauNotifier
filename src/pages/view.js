import { useEffect, useState } from 'react'
import { redirect, useNavigate } from 'react-router-dom';
import { API } from '../config';
import { Grid, TextField, Typography, FormControl, Button } from '@mui/material';

const isViolatedEmail = (email) => {
    const re = /\S+@\S+\.\S+/;
    return !re.test(email);
}

const isViolatedName = (name) => {
    const re = /^[a-zA-Z가-힣\s]+$/;
    return !re.test(name);
}

const View = () => {
    const navigate = useNavigate();

    const [formState, setFormState] = useState({
        emailState: {
            value: "",
            isViolated: false,
            helpText: "구독 시 입력했던 이메일을 입력해주세요.",
            errorText: "올바르지 않은 이메일 형식입니다."
        },
        nameState: {
            value: "",
            isViolated: false,
            helpText: "구독 시 입력했던 이름을 입력해주세요.",
            errorText: "이름은 한글과 영어로만 입력이 가능합니다."
        }
    })
    
    const [subscriptionData, setSubscriptionData] = useState({});

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

    const handleFormSubmitted = async () => {
        const email = formState.emailState.value;
        const name = formState.nameState.value;

        const isEmailViolated = isViolatedEmail(email);
        const isNameViolated = isViolatedName(name);

        setFormState((prev) => {
            const newObj = { ...prev };
            newObj.emailState.isViolated = isEmailViolated;
            newObj.nameState.isViolated = isNameViolated;
            return newObj;
        })

        if (isEmailViolated || isNameViolated) return;

        const result = await fetch(API.SUBSCRIPTIONS + `/${email}?name=${name}`, {
            headers: {
                "Content-Type": "application/json"
            },
            method: 'get'
        })

        const data = await result.json();

        if (data.result === "success") {
            navigate(`/subscription/${email}?name=${name}`)
        } else {
            alert(data.message);
        }
    }

    return (
        <>
            <Typography variant="h4" sx={{fontWeight: "bold"}} color="primary.main" gutterBottom>
                구독 조회
            </Typography>
            <FormControl sx={{minWidth: "100%"}}>
                <Grid container justifyContent="center" alignItems="center" spacing={2}>
                    <Grid item xs={12}>
                        <TextField 
                            id="email-field" 
                            fullWidth 
                            label="이메일"
                            type="email"
                            error={formState.emailState.isViolated}
                            helperText={formState.emailState.isViolated ? formState.emailState.errorText : formState.emailState.helpText} 
                            value={formState.emailState.value}
                            onChange={handleStateChange}
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <TextField 
                            id="name-field" 
                            fullWidth 
                            label="이름"
                            type='text'
                            error={formState.nameState.isViolated}
                            helperText={formState.nameState.isViolated ? formState.nameState.errorText : formState.nameState.helpText} 
                            value={formState.nameState.value}
                            onChange={handleStateChange}
                        />
                    </Grid>
                    <Grid item xs={8}></Grid>
                    <Grid item xs={4}>
                        <Button 
                            sx={{
                                height: "100%",
                                width: "100%"
                            }}
                            variant="outlined"
                            onClick={handleFormSubmitted}
                        >
                            조회
                        </Button>
                    </Grid>
                </Grid>
            </FormControl>
        </>
    )
}

export default View;