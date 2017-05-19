(ns metrics-tracker.db.schema)

(defn schema-def []
  [
   ;; Projects

   {:db/ident :project/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "The name of the project"}

   {:db/ident :project/type
    :db/valueType :db.type/keyword
    :db/cardinality :db.cardinality/one
    :db/doc "The type of the project"}

   {:db/ident :project/iterations
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/doc "The iterations in the project"}

   {:db/ident :project/id
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "The project ID - tool dependent, but always stored as a String"
    }

   ;; Pivotal Tracker project attributes
   {:db/ident :project.tracker/last-version
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "The last version of the project seen"
    }

   {:db/ident :project.tracker/api-token
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "The api token (and hence user) used to access the API"
    }

   ;; Iterations (Sprints)

   {:db/ident :iter/id
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "The iteration id"
    }

   {:db/ident :iter/state
    :db/valueType :db.type/keyword
    :db/cardinality :db.cardinality/one
    :db/doc "The iteration state: :done, :in-progress"
    }

   {:db/ident :iter/stories
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/doc "The stories of the iterations"
    }

   {:db/ident :iter/start-date
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc "The iteration start date"
    }

   {:db/ident :iter/end-date
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc "The iteration end date, missing if still in progress"
    }

   ;; Stories

   {:db/ident :story/id
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "The story id"
    }

   {:db/ident :story/state
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "The story state"
    }

   {:db/ident :story/estimate
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one
    :db/doc "The story estimate in story points"
    }

   {:db/ident :story/start-time
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc "The date the story was started"
    }

   {:db/ident :story/finished-time
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one
    :db/doc "The date the story was completed"
    }

   ])
