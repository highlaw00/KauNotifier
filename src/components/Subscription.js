import { useEffect, useState } from "react";
import { useLocation, useParams, useSearchParams } from "react-router-dom";
import { API } from "../config";
import Form from 'react-bootstrap/Form'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Button from 'react-bootstrap/Button'
import ListGroup from 'react-bootstrap/ListGroup';
import { useNavigate } from "react-router-dom";

const Subscription = () => {
    const params = useParams();
    const email = params.email;
    const [searchParams] = useSearchParams();
    const name = searchParams.get('name');

    const [isLoaded, setIsLoaded] = useState(false);
    const [message, setMessage] = useState("Loading...");
    const [user, setUser] = useState({});

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
                <ListGroup.Item key={elem.id}>{idx + 1}. {elem.description}</ListGroup.Item>
            )
        });
        return (
            <>
                <Form.Label >현재 구독중인 공지사항</Form.Label>
                <ListGroup className={"mb-2"}>
                    {elements}
                </ListGroup>
            </>
        )
    }

    return (
        isLoaded ? 
        <Form onSubmit={(e) => e.preventDefault()} noValidate>
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
            {/* <Checkboxes origins={origins} handleChange={handleCheckboxChange}></Checkboxes> */}
            {/* <Row>
                <Col><Button onClick={handleModify}>수정</Button></Col>
                <Col><Button onClick={handleDeletion}>삭제</Button></Col>
            </Row> */}
            <OriginList user={user}></OriginList>
            <Button onClick={handleModify}>수정</Button>
            <Button onClick={handleDeletion}>삭제</Button>
        </Form>
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