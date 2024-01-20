import React from 'react';
import ReactDOM from 'react-dom/client';
import 'mdb-react-ui-kit/dist/css/mdb.min.css';
import "@fortawesome/fontawesome-free/css/all.min.css";
import reportWebVitals from './reportWebVitals';
import UserForm from './UserForm';
import { MDBContainer, MDBRow, MDBCol } from 'mdb-react-ui-kit';
import useSWR from 'swr';

// created function to handle API request
const fetcher = (...args) => fetch(...args).then((res) => res.json());

const Swr = () => {
  const {
    data: users,
    error,
    isValidating,
  } = useSWR('http://localhost:9000/users', fetcher);

  // Handles error and loading state
  if (error) return <div className='failed'>failed to load</div>;
  if (isValidating) return <div className="Loading">Loading...</div>;

  return (
    <>
    <MDBContainer >
        <h3>Add new user</h3>
        <MDBRow>
            <MDBCol md='4'>
                <UserForm />
            </MDBCol>
        </MDBRow>
        <br/>
        <h3>User's List</h3>
        <div>
            <MDBCol md='8'>
            <table className="table">
            <thead>
                <tr>
                  <th scope="col">#</th>
                  <th scope="col">Name</th>
                  <th scope="col">Email</th>
                </tr>
              </thead>
              <tbody>
              {users &&
                users.map((user, index) => (
                  <User key={user.id} id={user.id} name={user.name} email={user.email} />
                ))}
              </tbody>
            </table>
            </MDBCol>
            <MDBCol md='4'>
                <a className="btn btn-secondary" id="btnExport" href="http://localhost:9000/export">Export Users</a>
            </MDBCol>
        </div>
    </MDBContainer>
    </>
  );
};


function User(user) {
  return (
      <tr>
        <th scope="row">{ user.id }</th>
        <td>{ user.name }</td>
        <td>{ user.email }</td>
      </tr>
  );
}



const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(<Swr />);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
