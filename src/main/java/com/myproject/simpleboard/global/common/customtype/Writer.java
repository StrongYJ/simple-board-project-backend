package com.myproject.simpleboard.global.common.customtype;

import com.myproject.simpleboard.member.Member;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Getter
public class Writer {
    
    private String writerName;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Writer(String writerName) {
        this.writerName = writerName;
    }

    public Writer(Member member) {
        this.writerName = member.getUsername();
        this.member = member;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((writerName == null) ? 0 : writerName.hashCode());
        result = prime * result + ((member == null) ? 0 : member.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Writer other = (Writer) obj;
        if (writerName == null) {
            if (other.writerName != null)
                return false;
        } else if (!writerName.equals(other.writerName))
            return false;
        if (member == null) {
            if (other.member != null)
                return false;
        } else if (!member.equals(other.member))
            return false;
        return true;
    }

    
}
