import { useEffect, useState } from 'react'
import Form from 'react-bootstrap/Form';
import InputGroup from 'react-bootstrap/InputGroup';
import Button from 'react-bootstrap/Button';
import { API } from '../config';
import { Checkboxes } from '../components/Checkboxes';
import { redirect, useNavigate } from 'react-router-dom';

const Subscribe = () => {
    const [origins, setOrigins] = useState({});
    const [selectedOrigins, setSelectedOrigins] = useState([]);
    const [isSent, setIsSent] = useState(false);
    const [isCertificated, setIsCertificated] = useState(false);
    const [email, setEmail] = useState("");
    const [name, setName] = useState("");
    const [code, setCode] = useState("");
    const navigate = useNavigate();

    // 공지사항 출처 불러오기
    useEffect(() => {
        // Get Origins and Update.
        fetch(API.SOURCES, {
            headers: {
                "Content-Type": "application/json"
            },
            method: 'get'
        })
        .then((response) => response.json())
        .then((data) => {
            setOrigins(data.sourceMap);
            console.log(origins);
        })
    }, [])

    const sendEmail = () => {
        fetch(API.SEND_MAIL, {
            headers: {
                "Content-Type": "application/json"
            },
            method: 'post',
            body: JSON.stringify({
                email: email
            })
        }).then((response) => response.json())
        .then((data) => {
            if (data.result === "success") {
                setIsSent(true);
                alert("인증 코드가 포함된 이메일이 발송되었습니다.");
            } else {
                alert(`인증 코드 전송에 실패하였습니다.\n${data.message}`);
            }
        });
    }

    const sendCode = () => {
        fetch(API.VERIFY, {
            headers: {
                "Content-Type": "application/json"
            },
            method: 'post',
            body: JSON.stringify({
                email: email,
                code: code
            })
        })
        .then((response) => response.json())
        .then((data) => {
            console.log(data);
            if (data.result === "success") {
                setIsCertificated(true);
                alert("인증이 완료되었습니다.");
            }
        });
    }

    const handleEmailChange = (e) => {
        setEmail(e.target.value);
    }

    const handleNameChange = (e) => {
        setName(e.target.value);
    }

    const handleCodeChange = (e) => {
        setCode(e.target.value);
    }

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

    const handleFormSubmitted = (e) => {
        e.preventDefault();
        const form = e.currentTarget;
        if (form.checkValidity() === false || selectedOrigins.length === 0) {
            e.preventDefault();
            e.stopPropagation();
            alert("Form invalid");
            return;
        }

        // 폼 제출
        submitForm();
    }

    const submitForm = async () => {
        const payload = {
            email: email,
            name: name,
            selectedSources: selectedOrigins,
            code: code
        };
        console.dir(payload);
    
        const response = await fetch(API.SUBSCRIBE, {
            headers: {
                "Content-Type": "application/json"
            },
            method: 'post',
            body: JSON.stringify(payload)
        })
        const data = await response.json();

        if (data.result === "success") {
            // redirect(`/subscription/${email}`)
            navigate(`/subscription/${email}?name=${name}`)
        } else {
            alert(data.message);
        }
    }

    return (
    <>
    <Form onSubmit={handleFormSubmitted} noValidate>
        <InputGroup className="mb-2">
            <Form.Control
                placeholder="이메일을 작성해주세요."
                value={email}
                required
                type="email"
                onChange={handleEmailChange}
            />
            <Button variant='outline-secondary' onClick={sendEmail}>
                인증 번호 발송
            </Button>
        </InputGroup>
        <InputGroup className="mb-3">
            <Form.Control
                placeholder="인증 코드를 작성해주세요."
                value={code}
                onChange={handleCodeChange}
            />
            {/* <Button variant='outline-secondary' onClick={sendCode}>
                인증하기
            </Button> */}
        </InputGroup>
        <Form.Control
            placeholder="이름을 작성해주세요."
            id="nameInput"
            aria-describedby="nameInputBlock"
            value={name}
            required
            type="text"
            onChange={handleNameChange}
            className="mb-2"
            />
        <Checkboxes origins={origins} handleChange={handleCheckboxChange}></Checkboxes>
        <Button onClick={handleFormSubmitted}>구독 신청</Button>
    </Form>
    </>
    )
}

export default Subscribe;