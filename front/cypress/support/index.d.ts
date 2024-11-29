/// <reference types="cypress" />

import { SessionInformation } from '../../src/app/interfaces/sessionInformation.interface';

declare namespace Cypress {
  interface Chainable {
    login(email: string, password: string, fixture: SessionInformation): Chainable<void>;
    detail: Chainable<void>;
  }
}
