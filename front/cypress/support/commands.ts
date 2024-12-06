// ***********************************************
// This example namespace declaration will help
// with Intellisense and code completion in your
// IDE or Text Editor.
// ***********************************************
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
import { Session } from '../../src/app/features/sessions/interfaces/session.interface';
import { Teacher } from '../../src/app/interfaces/teacher.interface';

/// <reference types="cypress" />

type FixtureFileName = string;

Cypress.Commands.add('login', (email: string, password: string, sessionFixture: FixtureFileName) => {
  cy.fixture<SessionInformation>(sessionFixture).then((fixture) => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: fixture
    }).as('loginRequest');
  });

  cy.fixture<Session[]>('yogaSessions.json').then((yogaSessions) => {
    cy.intercept('GET', '/api/session', (req) => {
      req.headers['Authorization'] = 'Bearer fake-jwt-token';
      req.reply({
        statusCode: 200,
        body: yogaSessions
      });
    }).as('getSessions');
  });

  cy.visit('/login');
  cy.get('input[formControlName=email]').type(email);
  cy.get('input[formControlName=password]').type(`${password}{enter}{enter}`);
  cy.wait('@loginRequest');
  cy.wait('@getSessions', { timeout: 5000 });
});

Cypress.Commands.add('detail', (yogaSessionFixture: FixtureFileName) => {
  cy.fixture<Session>(yogaSessionFixture).then((yogaSession) => {
    cy.intercept('GET', '/api/session/1', {
      statusCode: 200,
      body: yogaSession
    }).as('getSessionDetail');
  });

  cy.fixture<Teacher>('teacher.json').then((teacher) => {
    cy.intercept('GET', '/api/teacher/1', {
      statusCode: 200,
      body: teacher
    }).as('getTeacherDetail');
  });

  cy.scrollTo('bottom');
  cy.get('button').contains('Detail').click();
  cy.url().should('include', '/detail/1');
});

Cypress.Commands.add('create', () => {
  cy.fixture<Teacher[]>('teachers.json').then((teachers) => {
    cy.intercept('GET', '/api/teacher', {
      statusCode: 200,
      body: teachers
    }).as('getTeachers');
  });
  cy.get('button').contains('Create').click();
  cy.url().should('include', '/create');

});

Cypress.Commands.add('edit', () => {
  cy.scrollTo('bottom');
  cy.fixture<Session>('yogaSession.json').then((yogaSession) => {
    cy.intercept('GET', '/api/session/1', {
      statusCode: 200,
      body: yogaSession
    }).as('getSessionDetail');
  });
  cy.fixture<Teacher[]>('teachers.json').then((teachers) => {
    cy.intercept('GET', '/api/teacher', {
      statusCode: 200,
      body: teachers
    }).as('getTeachers');
  });
  cy.scrollTo('bottom');
  cy.get('button').contains('edit').click();
});

Cypress.Commands.add('me', () => {
  cy.get('button').contains('Account');
})
