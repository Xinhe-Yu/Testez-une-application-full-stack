// ***********************************************
// This example namespace declaration will help
// with Intellisense and code completion in your
// IDE or Text Editor.
// ***********************************************
// declare namespace Cypress {
//   interface Chainable<Subject = any> {
//     customCommand(param: any): typeof customCommand;
//   }
// }
//
// function customCommand(param: any): void {
//   console.warn(param);
// }
//
// NOTE: You can use it like so:
// Cypress.Commands.add('customCommand', customCommand);
//
// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })


import { SessionInformation } from '../../src/app/interfaces/sessionInformation.interface';

/// <reference types="cypress" />
Cypress.Commands.add('login', (email: string, password: string, fixture: SessionInformation) => {
  cy.intercept('POST', '/api/auth/login', {
    fixture: fixture, // Use the passed fixture
    statusCode: 200
  }).as('loginRequest');

  cy.intercept('GET', '/api/session', (req) => {
    req.headers['Authorization'] = 'Bearer fake-jwt-token';
    req.reply({
      statusCode: 200,
      body: [
        {
          id: 1,
          name: 'yoga',
          description: 'relax yourself',
          date: new Date('2024-11-20').toISOString(),
          teacher_id: 1,
          users: [],
        }
      ]
    });
  }).as('getSessions');

  cy.visit('/login');
  cy.get('input[formControlName=email]').type(email);
  cy.get('input[formControlName=password]').type(`${password}{enter}{enter}`);
  cy.wait('@loginRequest');
  cy.wait('@getSessions', { timeout: 5000 });
});

Cypress.Commands.add('detail', (yogaSession) => {
  cy.intercept('GET', '/api/session/1', { fixture: yogaSession }).as('getSessionDetail');
  cy.intercept('GET', '/api/teacher/1', { fixture: 'teacher.json' }).as('getTeacherDetail');
  cy.scrollTo('bottom');
  cy.get('button').contains('Detail').click();
  cy.url().should('include', '/detail/1');
});

Cypress.Commands.add('create', () => {
  cy.intercept('GET', '/api/teacher', { fixture: 'teachers.json' }).as('getTeachers');
  cy.get('button').contains('Create').click();
  cy.url().should('include', '/create');

});

Cypress.Commands.add('edit', () => {
  cy.scrollTo('bottom');
  cy.intercept('GET', '/api/session/1', { fixture: 'yogaSession.json' }).as('getSessionDetail');
  cy.intercept('GET', '/api/teacher', { fixture: 'teachers.json' }).as('getTeachers');

  cy.scrollTo('bottom');
  cy.get('button').contains('edit').click();
});

Cypress.Commands.add('me', () => {
  cy.get('button').contains('Account');
})
