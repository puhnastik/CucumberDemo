Feature: the data can be retrieved

  Scenario: client makes call to GET /fhir/data for version 1
    Given data exists in fhir for version 1
    When the client calls /fhir/data
    Then the client receives status code of 200
    And the client receives server version fhirdata1

  Scenario: client makes call to GET /fhir/data  for version 2
    Given data exists in fhir for version 2
    When the client calls /fhir/data
    Then the client receives status code of 200
    And the client receives server version fhirdata2

  Scenario: client makes call to GET /nantomics/data
    Given data exists in nantomics
    When the client calls /nantomics/data
    Then the client receives status code of 200
    And the client receives server version nantomicsdata



