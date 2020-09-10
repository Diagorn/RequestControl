package ru.mpei.requests.domain.requests;

import javax.persistence.*;

@Entity
public class PhysicalRequest extends Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public PhysicalRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public RequestState getStatus() {
        return status;
    }

    @Override
    public void setStatus(RequestState status) {
        this.status = status;
    }
}
