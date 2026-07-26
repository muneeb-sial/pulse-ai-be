
// const BASE_DIR = `src/main/java/com/example`
const BASE_DIR = `./src/main/java/app/Api`
module.exports = function (plop) {
  plop.setGenerator('module', {
    description: 'Generate a Java module with Controller, Service, Repository, and Entity',
    prompts: [
      {
        type: 'input',
        name: 'name',
        message: 'Module name (e.g. auth, user, order):',
      },
    ],
    actions: [
      {
        type: 'add',
        path: `${BASE_DIR}/{{properCase name}}/{{properCase name}}Controller.java`,
        templateFile: 'plop-templates/Controller.java.hbs',
      },
      {
        type: 'add',
        path: `${BASE_DIR}/{{properCase name}}/{{properCase name}}Service.java`,
        templateFile: 'plop-templates/Service.java.hbs',
      },
      {
        type: 'add',
        path: `${BASE_DIR}/{{properCase name}}/{{properCase name}}Repository.java`,
        templateFile: 'plop-templates/Repository.java.hbs',
      },
      {
        type: 'add',
        path: `${BASE_DIR}/{{properCase name}}/{{properCase name}}Entity.java`,
        templateFile: 'plop-templates/Entity.java.hbs',
      },
    ],
  });
};
