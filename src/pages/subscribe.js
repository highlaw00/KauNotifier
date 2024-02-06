import { useEffect, useState } from 'react'
import Form from 'react-bootstrap/Form';
import InputGroup from 'react-bootstrap/InputGroup';
import Button from 'react-bootstrap/Button';
import { API } from '../config';

const Checkboxes = ({origins, handleChange}) => {
    const checkboxes = []
    for (const id in origins) {
        const origin = origins[id];
        checkboxes.push(
            <div key={origin.id} className="mb-3">
                <Form.Check // prettier-ignore
                    type={`checkbox`}
                    id={origin.id}
                    label={origin.description}
                    value={origin.id}
                    onChange={handleChange}
                />
            </div>
        )
    }
    return checkboxes;
}

const Subscribe = () => {
    const [origins, setOrigins] = useState({});
    const [selectedOrigins, setSelectedOrigins] = useState([]);
    const [isSent, setIsSent] = useState(false);
    const [isCertificated, setIsCertificated] = useState(false);
    const [email, setEmail] = useState("");
    const [name, setName] = useState("");
    const [code, setCode] = useState("");

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
        if (isSent) alert("이미 전송되었습니다.");
        else {
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
                if (data.result === "success") setIsSent(true);
            });
        };
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
        // 인증 여부 확인
        if (!isCertificated) {
            alert("이메일 인증이 완료되지 않았습니다.");
            return;
        }
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

    const submitForm = () => {
        const payload = {
            email: email,
            name: name,
            selectedSources: selectedOrigins
        };
        console.dir(payload);
    
        fetch(API.SUBSCRIBE, {
            headers: {
                "Content-Type": "application/json"
            },
            method: 'post',
            body: JSON.stringify(payload)
        })
        .then((response) => response.json())
        .then((data) => console.log(data))
    }

    return (
    <>
    <Form onSubmit={handleFormSubmitted} noValidate>
        <InputGroup className="mt-3 mb-2">
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
        <InputGroup className="mb-4">
            <Form.Control
                placeholder="인증 코드를 작성해주세요."
                value={code}
                onChange={handleCodeChange}
            />
            <Button variant='outline-secondary' onClick={sendCode}>
                인증하기
            </Button>
        </InputGroup>
        <InputGroup className="mb-3">
            <Form.Control
                placeholder="이름을 작성해주세요."
                value={name}
                required
                type="text"
                onChange={handleNameChange}
            />
        </InputGroup>
        <Checkboxes origins={origins} handleChange={handleCheckboxChange}></Checkboxes>
        <Button onClick={handleFormSubmitted}>구독 신청</Button>
    </Form>
    </>
    )
}

export default Subscribe;