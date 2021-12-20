function fetchTimeout (url,options,timeout=3000) {
    return new Promise((resolve, reject) => {
        fetch(url, options)
            .then(resolve, reject)
        setTimeout(reject, timeout);
    })
}

async function sendRequest(method, url,body=null) {
    const headers = {
        'Content-Type': 'application/json;charset=utf-8'
    }
    if(body !== null) {
        body = JSON.stringify(body)
    }
    return await fetchTimeout(url, {
        method: method,
        body: body,
        headers: headers}, 3000).then(response => {
        if(response.ok) {
            return response.json()
        } else {
            return response.json().then(error => {
                const e = new Error('Что-то пошло не так')
                e.data = error
                throw e
            })
        }

    })
}

function delEmployee(id) {
    sendRequest('DELETE', 'http://localhost:8080/employees/' + id)
        .then(e => {
            alert('Сотрудник ' + e.firstName + ' ' + e.lastName + ' успешно удален')
        }).then(() => sendRequest('GET','http://localhost:8080/employees')
        .then(response => createDeleteEmployeeTab(response,'deleteEmployee')))
}


function addEmployee(idForm, selector) {
    let inputBody = {};
    let selectors =  document.getElementById(idForm).querySelectorAll(selector)
    selectors.forEach( input => {
        inputBody[input.id] = input.value
    })
    sendRequest('POST', 'http://localhost:8080/employees', inputBody)
        .then(() => alert('Сохранено'))
        .then(() => createAddEmployeeForm('addEmployee'))

}

function updateEmployee(idForm, selector, employeeId) {
    let inputBody = {};
    inputBody['employeeId'] = employeeId;
    let selectors =  document.getElementById(idForm).querySelectorAll(selector)
    selectors.forEach( input => {
        inputBody[input.id] = input.value
    })
    sendRequest('PUT', 'http://localhost:8080/employees', inputBody)
        .then(() => alert('Сохранено'))
        .then(() => sendRequest('GET','http://localhost:8080/employees')
            .then(employeesList => createUpdateEmployeeForm(employeesList,'updateEmployee')))
}


function getElement(id) {
    return document.getElementById(id, "deleteEmployee");
}

window.addEventListener("DOMContentLoaded", () => {

    const handlerClick = (event) => {
        if (event.target.id === "employees") {
            createHtmlElementsPageEmployees('divBody')
        }
    }

    getElement('employees').addEventListener('click', handlerClick)

});

function createHtmlElementsPageEmployees(idElement) {
    let element = getElement(idElement)
    element.innerHTML = '<ul class="nav nav-tabs" id="employeesTab" role="tablist">\n' +
        '  <li class="nav-item" role="presentation">\n' +
        '    <button data-num="1" class="nav-link" id="all-employeesTab" data-bs-toggle="tab" data-bs-target="#allEmployees" type="button" role="tab" aria-controls="allEmployees" aria-selected="false" >All Employees</button>\n' +
        '  </li>\n' +
        '  <li class="nav-item" role="presentation">\n' +
        '    <button class="nav-link" id="add-employeeTab" data-bs-toggle="tab" data-bs-target="#addEmployee" type="button" role="tab" aria-controls="addEmployee" aria-selected="false">Add Employee</button>\n' +
        '  </li>\n' +
        '  <li class="nav-item" role="presentation">\n' +
        '    <button class="nav-link" id="update-employeeTab" data-bs-toggle="tab" data-bs-target="#updateEmployee" type="button" role="tab" aria-controls="updateEmployee" aria-selected="false">Update Employee</button>\n' +
        '  </li>\n' +
        '  <li class="nav-item" role="presentation">\n' +
        '    <button class="nav-link" id="delete-employeeTab" data-bs-toggle="tab" data-bs-target="#deleteEmployee" type="button" role="tab" aria-controls="deleteEmployee" aria-selected="false">Delete Employee</button>\n' +
        '  </li>\n' +
        '</ul>\n' +
        '<div class="tab-content" id="employeesTabContent">\n' +
        '  <div class="tab-pane fade show active" id="allEmployees" role="tabpanel" aria-labelledby="allEmployess-tab">Отобразить всех сотрудников</div>\n' +
        '  <div class="tab-pane fade" id="addEmployee" role="tabpanel" aria-labelledby="addEmployee-tab">Добавить сотрудника</div>\n' +
        '  <div class="tab-pane fade" id="updateEmployee" role="tabpanel" aria-labelledby="updateEmployee-tab">Изменить данные сотрудника</div>\n' +
        '  <div class="tab-pane fade" id="deleteEmployee" role="tabpanel" aria-labelledby="deleteEmployee-tab">Удалить сотрудника</div>\n' +
        '</div>';

    addEventClick('#employeesTab li')

}

function addEventClick(idSelector) {
    const handlerClick = (event) => {
        switch (event.target.id) {
            case 'all-employeesTab':
                sendRequest('GET','http://localhost:8080/employees').then(response => {
                    createEmployeesTab(response, 'allEmployees', 'resultTableEmployees')
                })
                break
            case 'add-employeeTab':
                createAddEmployeeForm('addEmployee')
                break
            case 'update-employeeTab':
                sendRequest('GET','http://localhost:8080/employees').then(employeesList => {
                    createUpdateEmployeeForm(employeesList,'updateEmployee')
                })
                break
            case 'delete-employeeTab':
                sendRequest('GET','http://localhost:8080/employees').then(employeesList => {
                    createDeleteEmployeeTab(employeesList, 'deleteEmployee')
                })
                break
        }
    }

    let tableNav = document.querySelectorAll(idSelector)
    tableNav.forEach(button => {
        button.addEventListener('click', handlerClick)
    });

}

function  createDeleteEmployeeTab(employeesList, idElement) {

    let element = getElement(idElement)
    element.innerHTML = '<form id="formDeleteEmployee" class="row g-3"  >\n' +
        '<div class="col-md-4">\n' +
        '    <label for="delEmployeesList" class="form-label">Employee</label>\n' +
        '    <select id="delEmployeesList" class="form-select">\n' +
        '     <option selected>select employee</option>' +
        '    </select>\n' +
        '</div>' +
        '  <div class="col-12">\n' +
        '    <button type="button" id="deleteEmployee_btn" class="btn btn-primary">удалить</button>\n' +
        '  </div>\n' +
        '</form>';

    getOptions(employeesList, 'delEmployeesList')

    let button = document.querySelector('#deleteEmployee_btn')
    const handlerClick = () => {
        let selectedIndex = document.getElementById("delEmployeesList").options.selectedIndex;
        if(selectedIndex > 0 ) {
            let obj = employeesList[selectedIndex - 1]
            delEmployee(obj.employeeId)
        } else {
            alert("выберите сотрудника из списка")
        }
    }
    button.addEventListener('click', handlerClick)
}

function createEmployeesTab(data, idElement, idtable) {
    let element = getElement(idElement)
    let count = 1;
    element.innerHTML = '<table class="table">\n' +
        '  <thead>\n' +
        '    <tr>\n' +
        '      <th scope="col">#</th>\n' +
        '      <th scope="col">First name</th>\n' +
        '      <th scope="col">Last name</th>\n' +
        '      <th scope="col">Department id</th>\n' +
        '      <th scope="col">Job Title</th>\n' +
        '      <th scope="col">Gender</th>\n' +
        '      <th scope="col">Date of birth</th>\n' +
        '    </tr>\n' +
        '  </thead>\n' +
        '  <tbody id=' + idtable + '>\n' +
        '  </tbody>\n' +
        '</table>';
    data.forEach(e => {
        let tr = document.createElement('tr')
        tr.innerHTML = '<tr>\n' +
            '      <th scope="row">' + count++ +'</th>\n' +
            '      <td>' + e.firstName +'</td>\n' +
            '      <td>' + e.lastName +'</td>\n' +
            '      <td>' + e.departmentId +'</td>\n' +
            '      <td>' + e.jobTitle +'</td>\n' +
            '      <td>' + e.gender +'</td>\n' +
            '      <td>' + e.birthDate +'</td>\n' +
            '    </tr>';
        document.getElementById(idtable).appendChild(tr)
    })
}

function createAddEmployeeForm(idElement) {

    let element = getElement(idElement)
    element.innerHTML = '<form id="formAddEmployee" class="row g-3"  >\n' +
        '<div class="col-md-4">\n' +
        '    <label for="firstName" class="form-label">First name</label>\n' +
        '    <input type="text" class="form-control" placeholder="requried" id="firstName">\n' +
        '</div>\n' +
        '<div class="col-md-4">\n' +
        '    <label for="lastName" class="form-label">Last name</label>\n' +
        '    <input type="text" class="form-control" placeholder="requried" id="lastName">\n' +
        '</div>\n' +
        '<div class="col-md-2">\n' +
        '    <label for="gender" class="form-label">Gender</label>\n' +
        '    <select id="gender" class="form-select">\n' +
        '    <option selected></option>' +
        '    <option value="MALE">MALE</option>\n' +
        '    <option value="FEMALE">FEMALE</option>\n' +
        '    </select>\n' +
        '</div>\n' +
        '<div class="col-md-2">\n' +
        '    <label for="birthDate" class="form-label">Date of birth</label>\n' +
        '    <input type="date" class="form-control" id="birthDate">\n' +
        '</div>\n' +
        '<div class="col-md-4">\n' +
        '    <label for="jobTitle" class="form-label">Job title</label>\n' +
        '    <input type="text" class="form-control" id="jobTitle">\n' +
        '</div>\n' +
        '<div class="col-md-2">\n' +
        '    <label for="departmentId" class="form-label">Department id</label>\n' +
        '    <input type="number" class="form-control" id="departmentId">\n' +
        '</div>\n' +
        '  <div class="col-12">\n' +
        '    <button type="button" id="addEmployee_btn" class="btn btn-primary">сохранить</button>\n' +
        '  </div>\n' +
        '</form>';

    let button = document.querySelector('#addEmployee_btn')
    const handlerClick = () => {
        addEmployee("formAddEmployee",["input","select"])
    }
    button.addEventListener('click', handlerClick)
}

function createUpdateEmployeeForm(employeesList,idElement) {

    let element = getElement(idElement)
    element.innerHTML = '<form id="formUpdateEmployee" class="row g-3"  >\n' +
        '<div class="col-md-4">\n' +
        '    <label for="employeesList" class="form-label">Employee</label>\n' +
        '    <select id="employeesList" class="form-select">\n' +
        '     <option selected>select employee</option>' +
        '    </select>\n' +
        '</div>' +
        '  <div class="col-12">\n' +
        '    <button type="button" id="updateEmployee_btn" class="btn btn-primary">выбрать</input>\n' +
        '  </div>\n' +
        '</form>';

    getOptions(employeesList, 'employeesList')

    let button = document.querySelector('#updateEmployee_btn')
    const handlerClick = () => {
        let selectedIndex = document.getElementById("employeesList").options.selectedIndex;
        if(selectedIndex > 0 ) {
            let obj = employeesList[selectedIndex - 1]
            createUpdateEmployeeForm2(idElement, obj)
        } else {
            alert("выберите сотрудника из списка")
        }
    }
    button.addEventListener('click', handlerClick)
}

function createUpdateEmployeeForm2(idElement, obj) {

    let element = getElement(idElement)
    element.innerHTML = '<form id="formUpdateEmployee" class="row g-3"  >\n' +
        '<div class="col-md-4">\n' +
        '    <label for="firstName" class="form-label">First name</label>\n' +
        '    <input type="text" class="form-control" placeholder="requried" id="firstName">\n' +
        '</div>\n' +
        '<div class="col-md-4">\n' +
        '    <label for="lastName" class="form-label">Last name</label>\n' +
        '    <input type="text" class="form-control" placeholder="requried" id="lastName" >\n' +
        '</div>\n' +
        '<div class="col-md-2">\n' +
        '    <label for="gender" class="form-label">Gender</label>\n' +
        '    <select id="gender" class="form-select">\n' +
        '    <option selected disabled value=""></option>' +
        '    <option value="MALE">MALE</option>\n' +
        '    <option value="FEMALE">FEMALE</option>\n' +
        '    </select>\n' +
        '</div>\n' +
        '<div class="col-md-2">\n' +
        '    <label for="birthDate" class="form-label">Date of birth</label>\n' +
        '    <input type="date" class="form-control" id="birthDate">\n' +
        '</div>\n' +
        '<div class="col-md-4">\n' +
        '    <label for="jobTitle" class="form-label">Job title</label>\n' +
        '    <input type="text" class="form-control" id="jobTitle">\n' +
        '</div>\n' +
        '<div class="col-md-2">\n' +
        '    <label for="departmentId" class="form-label">Department id</label>\n' +
        '    <input type="number" class="form-control" id="departmentId">\n' +
        '</div>\n' +
        '  <div class="col-12">\n' +
        '    <button type="button" id="updateEmployee_btn" class="btn btn-primary">сохранить</button>\n' +
        '  </div>\n' +
        '</form>';

    insertValueForm("formUpdateEmployee",["input","select"], obj)
    let button = document.querySelector('#updateEmployee_btn')
    const handlerClick = () => {
        updateEmployee("formUpdateEmployee",["input","select"], obj.employeeId)
    }
    button.addEventListener('click', handlerClick)
}

function getOptions(data, element) {
    data.forEach((e) => {
        let options = document.createElement("option")
        options.innerHTML = '<option>' + e.firstName + " " + e.lastName + '</option>';
        document.getElementById(element).appendChild(options)
    })
}

function insertValueForm(idForm, selectors, obj) {
    let elements = document.getElementById(idForm);
    selectors.forEach( selectors => {
        let selector =  elements.querySelectorAll(selectors)
        selector.forEach( input => {
            input.value = obj[input.id]
        })
    })
}