import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

export default function UserForm() {
  const [formData, setFormData] = useState({name: "",email: ""});

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevFormData) => ({ ...prevFormData, [name]: value }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    if (`${formData.name}` === '' || `${formData.email}` === '') {
        alert("Please enter values")
        return;
    }
    fetch('http://localhost:9000/v1/users', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        name: `${formData.name}`,
        email: `${formData.email}`
      })
    });
};

  return (
    <div className="form-group">
        <form onSubmit={handleSubmit} className="form">
          <label htmlFor="name">Name</label>
          <input className="form-control" type="text" id="name" name="name" value={formData.name} onChange={handleChange}/>

          <label htmlFor="email">Email</label>
          <input className="form-control" type="email" id="email" name="email" value={formData.email} onChange={handleChange}/>

          <button className="btn btn-primary" type="submit">Submit</button>
        </form>
    </div>
  );
}