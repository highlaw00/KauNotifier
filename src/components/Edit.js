import { useEffect, useState } from "react";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";
import { API } from "../config";
import Form from 'react-bootstrap/Form'
import { Checkboxes } from "./Checkboxes";
import Button from 'react-bootstrap/Button'

const Edit = () => {
    const params = useParams();
    const email = params.email;
    const [searchParams] = useSearchParams();
    const name = searchParams.get('name');

    const [isLoaded, setIsLoaded] = useState(false);
    const [message, setMessage] = useState("Loading...");
    const [user, setUser] = useState({});

    const [origins, setOrigins] = useState({});
    const [selectedOrigins, setSelectedOrigins] = useState([]);

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
                setOrigins(data.sourceMap);
            } catch (e) {
                console.err(e);
            }
        }
        getOrigins();
        getMember();
    }, [])

    // 체크박스 클릭시 selectedSources 변경
    const handleCheckboxChange = (e) => {
        const selectedOriginId = e.target.value;
        const isChecked = e.target.checked;
        // 선택한 경우
        if (isChecked) {
            setSelectedOrigins(prevOrigins => [...prevOrigins, selectedOriginId])
        }
        // 선택해제한 경우 
        else {
            setSelectedOrigins(prevOrigins => prevOrigins.filter((originId) => originId !== selectedOriginId));
        }
    }

    const handleFormSubmitted = async (e) => {
        e.preventDefault();
        const payload = {
            email: user.email,
            name: user.name,
            selectedSources: selectedOrigins
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
        <Form onSubmit={handleFormSubmitted} noValidate>
            <Form.Label htmlFor="inputEmail">이메일 주소</Form.Label>
            <Form.Control
                id="inputEmail"
                value={user.email}
                required
                readOnly
                type="email"
                className="mb-2"
            />
            <Form.Label htmlFor="inputName">이름</Form.Label>
            <Form.Control
                id="inputName"
                value={user.name}
                required
                readOnly
                type="text"
                className="mb-2"
                />
            <Checkboxes origins={origins} handleChange={handleCheckboxChange}></Checkboxes>
            <Button onClick={handleFormSubmitted}>구독 수정</Button>
        </Form>
        : <h1>{message}</h1>
    )
}

export default Edit;