package app.Configs.Security.Jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtDto {
    private String userId;
    private String email;
    private String role;
}
