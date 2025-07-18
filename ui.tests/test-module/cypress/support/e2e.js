/*
 *  Copyright 2023 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import './commands'
require('cypress-terminal-report/src/installLogsCollector')({
    xhr: {
        printHeaderData: false,
        printRequestData: false,
    },
    debug: true
});

before(() => {
  const baseUrl = Cypress.config('baseUrl');

  // Add your publish server hostname(s) or ports here
  const isPublishEnv = baseUrl.includes('4503') || baseUrl.includes('publish') || baseUrl.includes('prod');

  if (isPublishEnv) {
    cy.log('Skipping Cypress tests in publish environment:', baseUrl);
    Cypress.runner.stop(); // Hard stop — won't run anything
  }
});
