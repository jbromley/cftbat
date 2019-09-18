(ns fwpd.core-test
  (:require [clojure.test :refer :all]
            [fwpd.core :refer :all]))

(deftest suspect-name-test
  (testing "Convert list of suspect records to a list of names"
    (is (= (find-suspects filename 3)
           '("Edward Cullen" "Jacob Black" "Carlisle Cullen")))))

(deftest failing-test
  (testing "This test always fails."
    (is (= 0 1))))
