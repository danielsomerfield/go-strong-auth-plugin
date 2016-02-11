Feature: Go Strong Auth

  Scenario: Baseline test
    Given Go CD is running
    Given Auth is disabled
    When I make a request to the go home page
    Then I see the go home page

  Scenario: Auth enabled
    Given Go CD is running
    Given Auth is enabled
    When I make a request to the go home page
    Then I am redirected to the login screen