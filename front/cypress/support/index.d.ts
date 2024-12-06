/// <reference types="cypress" />

declare namespace Cypress {
  type FixtureFileName = string;

  interface Chainable<Subject> {
    login(email: string, password: string, sessionFixture: FixtureFileName): Chainable<void>;
    detail(yogaSession: FixtureFileName): Chainable<void>;
    create(): Chainable<void>;
    edit(): Chainable<void>;
    me(): Chainable<void>;
  }
}
