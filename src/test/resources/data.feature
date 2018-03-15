Feature: the data can be retrieved

  Scenario: client makes call to GET /fhir/data
    When the client calls /fhir/data
    Then the client receives status code of 200
    And the client receives server version fhirdata

  Scenario: client makes call to GET /nantomics/data
    When the client calls /nantomics/data
    Then the client receives status code of 200
    And the client receives server version nantomicsdata

