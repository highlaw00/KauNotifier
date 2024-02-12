import { useEffect, useState } from 'react'
import { redirect, useNavigate } from 'react-router-dom';
import Form from 'react-bootstrap/Form';
import InputGroup from 'react-bootstrap/InputGroup';
import Button from 'react-bootstrap/Button';
import { API } from '../config';

const FindForm = (props) => {
    const {email, handleEmailChange, name, handleNameChange, handleFormSubmitted} = props;
    return (
        <Form onSubmit={handleFormSubmitted} noValidate>
            <InputGroup className="mb-2"> 
                <Form.Control
                    placeholder="이메일을 작성해주세요."
                    value={email}
                    required
                    type="email"
                    onChange={handleEmailChange}
                />
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
            <Button onClick={handleFormSubmitted}>구독 조회</Button>
        </Form>
    );
}

const View = () => {
    const [email, setEmail] = useState("");
    const [name, setName] = useState("");
    const [subscriptionData, setSubscriptionData] = useState({});
    const navigate = useNavigate();

    const handleEmailChange = (e) => {
        setEmail(e.target.value);
    }
    const handleNameChange = (e) => {
        setName(e.target.value);
    }
    const handleFormSubmitted = async () => {
        const result = await fetch(API.SUBSCRIPTIONS + `/${email}?name=${name}`, {
            headers: {
                "Content-Type": "application/json"
            },
            method: 'get'
        })

        const data = await result.json();

        if (data.result === "success") {
            // return redirect("/")
            navigate(`/subscription/${email}?name=${name}`)
        } else {
            alert(data.message);
        }
    }

    return (
        <FindForm email={email} handleEmailChange={handleEmailChange} 
            handleNameChange={handleNameChange} handleFormSubmitted={handleFormSubmitted}>
        </FindForm>
    )
}

export default View;