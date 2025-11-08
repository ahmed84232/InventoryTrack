package com.yasser.InventoryTrack.util;


import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@Component
@RequestScope
@Data
public class SecurityContext {
    String userId;
    String userName;
    List<String> roles;
}
