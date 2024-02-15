import Form from 'react-bootstrap/Form';

export const Checkboxes = ({ origins, handleChange }) => {
    const checkboxes = [];
    for (const id in origins) {
        const origin = origins[id];
        checkboxes.push(
            <div key={origin.id} className="mb-3">
                <Form.Check // prettier-ignore
                    type={`checkbox`}
                    id={origin.id}
                    label={origin.description}
                    value={origin.id}
                    onChange={handleChange} />
            </div>
        );
    }
    return checkboxes;
};
